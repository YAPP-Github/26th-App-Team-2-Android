package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.breake.core.permission.PermissionType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.ui.SnackBarState
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginNavState
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
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

	private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.LoginIdle)
	val uiState = _uiState.asStateFlow()

	private val _snackBarFlow = MutableSharedFlow<SnackBarState>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<LoginNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private var loginJob: Job? = null

	fun showPrivacyPolicy() {
		viewModelScope.launch {
			_navigationFlow.emit(LoginNavState.NavigateToPrivacyPolicy)
		}
	}

	fun showTermsOfService() {
		viewModelScope.launch {
			_navigationFlow.emit(LoginNavState.NavigateToTermsOfService)
		}
	}

	fun loginWithKakao() {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginOnWebView
		}
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, "kakao_webView_activity")
		}
	}

	fun authCancel() {
		_uiState.value = LoginUiState.LoginIdle
		firebaseAnalytics.apply {
			logEvent("cancel_kakao_login") {
				param("reason", "user_cancel")
			}
			logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
			}
		}
	}

	fun loginCancel() {
		loginJob?.run {
			cancel()
			_uiState.value = LoginUiState.LoginIdle
		}
		firebaseAnalytics.apply {
			logEvent("cancel_server_login") {
				param("reason", "user_cancel")
			}
			logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
			}
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
						SnackBarState.Error(
							uiString = UiString.ResourceString(
								resId = R.string.login_snackbar_login_error,
							),
						),
					)
					firebaseAnalytics.logEvent("cancel_server_login") {
						param("reason", "server_error")
					}
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
							SnackBarState.Error(
								uiString = UiString.ResourceString(
									resId = R.string.login_snackbar_login_error_inactive,
								),
							),
						)
						firebaseAnalytics.logEvent("cancel_server_login") {
							param("reason", "server_not_allowed")
						}
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
				SnackBarState.Error(
					uiString = UiString.DynamicString(message),
				),
			)
		}
		firebaseAnalytics.apply {
			logEvent("cancel_kakao_login") {
				param("reason", message)
			}
			logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
			}
		}
	}

	private suspend fun decideNextDestination(context: Context) {
		val status = decideDestinationUseCase(
			onError = { error ->
				_snackBarFlow.emit(
					SnackBarState.Error(
						uiString = UiString.ResourceString(
							resId = R.string.login_snackbar_next_destination_error,
						),
					),
				)
				firebaseAnalytics.apply {
					logEvent("cancel_client_login") {
						param("reason", "client_error")
					}
					logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
						param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
					}
				}
			},
		)
		when (status) {
			is Destination.PermissionOrHome -> if (checkPermissions(context)) {
				_navigationFlow.emit(LoginNavState.NavigateToHome)
				firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
					param(FirebaseAnalytics.Param.METHOD, "user_login")
				}
			} else {
				_navigationFlow.emit(LoginNavState.NavigateToPermission)
				firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
					param(FirebaseAnalytics.Param.METHOD, "user_login")
				}
			}

			is Destination.Onboarding -> {
				_navigationFlow.emit(
					LoginNavState.NavigateToOnboarding,
				)
				firebaseAnalytics.apply {
					logEvent(FirebaseAnalytics.Event.LOGIN) {
						param(FirebaseAnalytics.Param.METHOD, "user_login")
					}
					logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null)
				}
			}

			else -> {}
		}
	}
}
