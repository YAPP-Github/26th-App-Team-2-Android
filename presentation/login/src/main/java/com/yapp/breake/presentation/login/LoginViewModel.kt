package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.breake.core.permission.PermissionType
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginNavState
import com.yapp.breake.presentation.login.model.LoginSnackBarState
import com.yapp.breake.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
	private val loginUseCase: LoginUseCase,
	private val decideDestinationUseCase: DecideNextDestinationFromPermissionUseCase,
	private val permissionManager: PermissionManager,
) : ViewModel() {

	private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.LoginIdle)
	val uiState = _uiState.asStateFlow()

	private val _snackBarFlow = MutableSharedFlow<LoginSnackBarState>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<LoginNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private var loginJob: Job? = null

	fun loginWithKakao() {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginOnWebView
		}
	}

	fun authCancel() {
		_uiState.value = LoginUiState.LoginIdle
	}

	fun loginCancel() {
		loginJob?.run {
			cancel()
			_uiState.value = LoginUiState.LoginIdle
		}
	}

	fun authSuccess(authCode: String, context: Context) {
		loginJob?.cancel()
		loginJob = viewModelScope.launch {
			_uiState.value = LoginUiState.LoginLoading
			loginUseCase(
				authCode = authCode,
				onError = { throwable ->
					_uiState.value = LoginUiState.LoginIdle
					_snackBarFlow.emit(
						LoginSnackBarState.Error(
							uiString = UiString.ResourceString(
								resId = R.string.login_snackbar_login_error,
							),
						),
					)
				},
			).catch {
				Timber.e(it, "로그인 중 에러 발생")
			}.collect { result ->
				when (result) {
					UserStatus.ACTIVE -> {
						_uiState.value = LoginUiState.LoginIdle
						decideNextDestination(context)
					}

					UserStatus.HALF_SIGNUP -> {
						_uiState.value = LoginUiState.LoginIdle
						_navigationFlow.emit(LoginNavState.NavigateToSignup)
					}

					UserStatus.INACTIVE -> {
						_uiState.value = LoginUiState.LoginIdle
						_snackBarFlow.emit(
							LoginSnackBarState.Error(
								uiString = UiString.ResourceString(
									resId = R.string.login_snackbar_login_error_inactive,
								),
							),
						)
					}
				}
			}
		}
	}

	fun checkPermissions(context: Context): Boolean {
		if (!permissionManager.isGranted(context, PermissionType.OVERLAY)) return false
		if (!permissionManager.isGranted(context, PermissionType.STATS)) return false
		if (!permissionManager.isGranted(context, PermissionType.EXACT_ALARM)) return false
		if (!permissionManager.isGranted(context, PermissionType.ACCESSIBILITY)) return false
		return true
	}

	fun loginFailure(message: String) {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginIdle
			_snackBarFlow.emit(
				LoginSnackBarState.Error(
					uiString = UiString.DynamicString(message),
				),
			)
		}
	}

	private suspend fun decideNextDestination(context: Context) {
		val status = decideDestinationUseCase(
			onError = { error ->
				_snackBarFlow.emit(
					LoginSnackBarState.Error(
						uiString = UiString.ResourceString(
							resId = R.string.login_snackbar_next_destination_error,
						),
					),
				)
			},
		)
		when (status) {
			is Destination.PermissionOrHome -> if (checkPermissions(context)) {
				_navigationFlow.emit(LoginNavState.NavigateToHome)
			} else {
				_navigationFlow.emit(LoginNavState.NavigateToPermission)
			}

			is Destination.Onboarding -> {
				_navigationFlow.emit(
					LoginNavState.NavigateToOnboarding,
				)
			}

			else -> {}
		}
	}
}
