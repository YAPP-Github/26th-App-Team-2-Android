package com.teambrake.brake.presentation.login

import android.content.Context
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.auth.google.GoogleAuthManager
import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.core.permission.PermissionManager
import com.teambrake.brake.core.permission.PermissionType
import com.teambrake.brake.core.ui.SnackBarState
import com.teambrake.brake.core.ui.UiString
import com.teambrake.brake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.teambrake.brake.domain.usecase.LoginUseCase
import com.teambrake.brake.domain.usecase.StartOfflineModeUseCase
import com.teambrake.brake.presentation.login.model.LoginNavState
import com.teambrake.brake.presentation.login.model.LoginUiState
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
	private val startOfflineModeUseCase: StartOfflineModeUseCase,
	private val permissionManager: PermissionManager,
	private val googleAuthManager: GoogleAuthManager,
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

	fun loginWithGoogle(context: Context, authCode: String) {
		loginService(context, authCode, GOOGLE_LOGIN)
	}

	fun getGoogleAuthorization(
		context: Context,
		onRequestGoogleAuth: (IntentSenderRequest) -> Unit,
	) {
		_uiState.value = LoginUiState.LoginLoading
		googleAuthManager.requestGoogleAuthorization(
			context = context,
			onRequestGoogleAuth = onRequestGoogleAuth,
			onFailure = {
				cancelGoogleAuthorization()
			},
			onAlertUpdateGooglePlayServices = ::alterGoogleServicesUpdate,
		)
	}

	fun startOfflineMode() {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginLoading
			startOfflineModeUseCase.invoke(
				offlineNickname = R.string.offline_mode_username_default.toString(),
				onError = { throwable ->
					_uiState.value = LoginUiState.LoginIdle
					_snackBarFlow.emit(
						SnackBarState.Error(
							uiString = UiString.ResourceString(
								resId = R.string.login_snackbar_login_error,
							),
						),
					)
					Timber.e(throwable, "오프라인 모드 시작 중 에러 발생")
					firebaseAnalytics.apply {
						logEvent("cancel_client_offline_mode") {
							param("reason", "client_error")
						}
						logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
							param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
						}
					}
				},
			).let { destination ->
				when (destination) {
					is Destination.PermissionOrHome -> {
						_navigationFlow.emit(LoginNavState.NavigateToPermission)
					}

					is Destination.Onboarding -> {
						_navigationFlow.emit(
							LoginNavState.NavigateToOnboarding,
						)
						firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null)
					}

					else -> {
						_uiState.value = LoginUiState.LoginIdle
						_snackBarFlow.emit(
							SnackBarState.Error(
								uiString = UiString.ResourceString(
									resId = R.string.login_snackbar_offline_mode_error,
								),
							),
						)
					}
				}
			}
		}
	}

	fun cancelGoogleAuthorization() {
		_uiState.value = LoginUiState.LoginIdle
	}

	fun failGoogleAuthorization() {
		_uiState.value = LoginUiState.LoginIdle
		viewModelScope.launch {
			_snackBarFlow.emit(
				SnackBarState.Error(
					uiString = UiString.ResourceString(
						resId = R.string.login_snackbar_login_error,
					),
				),
			)
		}
	}

	private fun alterGoogleServicesUpdate() {
		_uiState.value = LoginUiState.LoginIdle
		viewModelScope.launch {
			_snackBarFlow.emit(
				SnackBarState.Error(
					uiString = UiString.ResourceString(
						resId = R.string.login_snackbar_login_error_need_google_services_update,
					),
				),
			)
		}
	}

	fun loginWithKakao(context: Context, authCode: String) {
		loginService(context, authCode, KAKAO_LOGIN)
	}

	fun getKakaoAuthorization() {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginOnWebView
		}
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, "kakao_webView_activity")
		}
	}

	fun cancelKakaoAuthorization() {
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

	fun failKakaoAuthorization(message: String) {
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

	fun cancelLogin() {
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

	private fun loginService(context: Context, authCode: String, provider: String) {
		loginJob?.cancel()
		loginJob = viewModelScope.launch {
			_uiState.value = LoginUiState.LoginLoading
			loginUseCase(
				authCode = authCode,
				provider = provider,
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
					UserStatus.OFFLINE -> {
						_uiState.value = LoginUiState.LoginIdle
						_snackBarFlow.emit(
							SnackBarState.Error(
								uiString = UiString.ResourceString(
									resId = R.string.login_snackbar_login_error_offline,
								),
							),
						)
						firebaseAnalytics.logEvent("cancel_server_login") {
							param("reason", "server_offline")
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

	companion object {
		const val GOOGLE_LOGIN = "GOOGLE"
		const val KAKAO_LOGIN = "KAKAO"
	}
}
