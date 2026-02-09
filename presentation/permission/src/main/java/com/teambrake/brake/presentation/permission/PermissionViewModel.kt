package com.teambrake.brake.presentation.permission

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.android.Amplitude
import com.teambrake.brake.presentation.permission.model.PermissionItem
import com.teambrake.brake.presentation.permission.model.PermissionModalState
import com.teambrake.brake.presentation.permission.model.PermissionNavState
import com.teambrake.brake.presentation.permission.model.PermissionUiState
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.amplitude.StepDetail
import com.teambrake.brake.core.amplitude.StepName
import com.teambrake.brake.core.permission.PermissionManager
import com.teambrake.brake.core.permission.PermissionType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.ui.UiString
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.teambrake.brake.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
	private val decideDestinationUseCase: DecideNextDestinationFromPermissionUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
	private val amplitude: Amplitude,
) : ViewModel() {

	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _uiState =
		MutableStateFlow(PermissionUiState(permissions = persistentListOf()))
	val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<PermissionNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private val _modalFlow =
		MutableStateFlow<PermissionModalState>(PermissionModalState.PermissionIdle)
	val modalFlow = _modalFlow.asStateFlow()

	/**
	 * 권한 요청 시 view_onboarding 이벤트 전송
	 * @param permissionItem 요청하는 권한 타입
	 */
	private fun trackPermissionView(permissionItem: PermissionItem) {
		val stepDetail = when (permissionItem) {
			PermissionItem.OVERLAY -> StepDetail.OVERLAY
			PermissionItem.STATS -> StepDetail.USAGE
			PermissionItem.EXACT_ALARM -> StepDetail.NOTIFICATION
			PermissionItem.ACCESSIBILITY -> StepDetail.ACCESSIBILITY
		}

		val event = AmplitudeEventHelper.createViewOnboardingEvent(
			stepName = StepName.PERMISSION,
			stepDetail = stepDetail,
		)
		amplitude.track(event.getEventName(), event.toEventProperties())
	}

	private fun stackPermissions(context: Context): PersistentList<PermissionItem> {
		try {
			val permissionList = mutableListOf<PermissionItem>()
			if (!permissionManager.isGranted(context, PermissionType.OVERLAY)) {
				permissionList.add(PermissionItem.OVERLAY)
			}
			if (!permissionManager.isGranted(context, PermissionType.STATS)) {
				permissionList.add(PermissionItem.STATS)
			}
			if (!permissionManager.isGranted(context, PermissionType.EXACT_ALARM)) {
				permissionList.add(PermissionItem.EXACT_ALARM)
			}
			if (!permissionManager.isGranted(context, PermissionType.ACCESSIBILITY)) {
				permissionList.add(PermissionItem.ACCESSIBILITY)
			}
			return permissionList.toPersistentList()
		} catch (_: Exception) {
			viewModelScope.launch {
				_snackBarFlow.emit(
					UiString.ResourceString(R.string.permission_snackbar_permission_stack_error),
				)
			}
			// 모든 권한을 포함
			return persistentListOf(
				PermissionItem.OVERLAY,
				PermissionItem.STATS,
				PermissionItem.EXACT_ALARM,
				PermissionItem.ACCESSIBILITY,
			)
		}
	}

	private fun decideNextDestination() {
		viewModelScope.launch {
			when (val result = decideDestinationUseCase()) {
				is BrakeResult.Error -> {
					_snackBarFlow.emit(
						UiString.ResourceString(R.string.permission_snackbar_permission_stack_error),
					)
				}

				is BrakeResult.Success -> {
					firebaseAnalytics.logEvent("done_permission", null)
					when (result.data) {
						Destination.PermissionOrHome -> {
							_navigationFlow.emit(PermissionNavState.NavigateToMain)
						}

						Destination.Onboarding -> {
							_navigationFlow.emit(PermissionNavState.NavigateToComplete)
						}

						else -> {}
					}
				}
			}
		}
	}

	fun refreshPermissions(context: Context) {
		val permissionList = stackPermissions(context)
		if (permissionList.isEmpty()) {
			decideNextDestination()
		} else {
			_uiState.value = PermissionUiState(permissions = permissionList)
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "permission_screen")
			}
		}
	}

	fun requestPermission(context: Context, type: PermissionItem) {
		// 권한 요청 화면 진입 시 트래킹
		trackPermissionView(type)

		if (type == PermissionItem.ACCESSIBILITY) {
			_modalFlow.value = PermissionModalState.ShowAccessibilityAgreementModal
		} else {
			viewModelScope.launch {
				_navigationFlow.emit(
					PermissionNavState.RequestPermission(
						permissionManager.getIntent(context, parsePermissionItemToType(type)),
					),
				)
			}
		}
	}

	fun requestAccessibilityPermission(context: Context) {
		_modalFlow.value = PermissionModalState.PermissionIdle
		viewModelScope.launch {
			_navigationFlow.emit(
				PermissionNavState.RequestPermission(
					permissionManager.getIntent(context, PermissionType.ACCESSIBILITY),
				),
			)
		}
	}

	fun tryLogout() {
		_modalFlow.value = PermissionModalState.ShowLogoutModal
	}

	fun dismissModal() {
		_modalFlow.value = PermissionModalState.PermissionIdle
	}

	fun logout() {
		viewModelScope.launch {
			when (logoutUseCase()) {
				is BrakeResult.Success -> {
					firebaseAnalytics.logEvent("app_logout") {
						param(FirebaseAnalytics.Param.METHOD, "user_logout")
					}
					_navigationFlow.emit(PermissionNavState.NavigateToLogin)
				}

				is BrakeResult.Error -> {
					_snackBarFlow.emit(
						UiString.ResourceString(
							resId = R.string.permission_snackbar_logout_error,
						),
					)
				}
			}
		}
	}

	fun popBackStack() {
		viewModelScope.launch {
			_navigationFlow.emit(PermissionNavState.NavigateToBack)
		}
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, "onboarding_guide_screen")
		}
	}

	companion object {
		private val parsePermissionItemToType = { permissionItem: PermissionItem ->
			when (permissionItem) {
				PermissionItem.OVERLAY -> PermissionType.OVERLAY
				PermissionItem.STATS -> PermissionType.STATS
				PermissionItem.EXACT_ALARM -> PermissionType.EXACT_ALARM
				PermissionItem.ACCESSIBILITY -> PermissionType.ACCESSIBILITY
			}
		}
	}
}
