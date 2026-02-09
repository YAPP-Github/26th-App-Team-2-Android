package com.teambrake.brake.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.auth.google.GoogleAuthManager
import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.ui.SnackBarState
import com.teambrake.brake.core.ui.UiString
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.usecase.DeleteAccountUseCase
import com.teambrake.brake.domain.usecase.GetNicknameUseCase
import com.teambrake.brake.domain.usecase.LogoutUseCase
import com.teambrake.brake.presentation.setting.model.SettingEffect
import com.teambrake.brake.presentation.setting.model.SettingUiState
import com.teambrake.brake.presentation.setting.model.SettingUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	getNicknameUseCase: GetNicknameUseCase,
	private val deleteAccountUseCase: DeleteAccountUseCase,
	private val logoutUseCase: LogoutUseCase,
	private val googleAuthManager: GoogleAuthManager,
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

	private var deleteJob: Job? = null

	private val _snackBarFlow = MutableSharedFlow<SnackBarState>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _uiState = MutableStateFlow(SettingUiState.Idle)
	internal val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<SettingEffect>()
	internal val navigationFlow = _navigationFlow.asSharedFlow()

	init {
		viewModelScope.launch {
			getNicknameUseCase().first { result ->
				when (result) {
					is BrakeResult.Success -> {
						_uiState.update {
							SettingUiState(
								user = SettingUser(
									imageUrl = null,
									name = result.data,
								),
								appInfo = _uiState.value.appInfo,
								status = SettingUiState.Status.Loaded,
							)
						}
						true
					}
					is BrakeResult.Error -> {
						Timber.e("Failed to get nickname: ${result.error}")
						_snackBarFlow.emit(
							SnackBarState.Error(
								uiString = UiString.ResourceString(R.string.snackbar_get_nickname_error),
							),
						)
						false
					}
				}
			}
		}
	}

	override fun onCleared() {
		super.onCleared()
		deleteJob?.cancel()
	}

	fun modifyNickname() {
		viewModelScope.launch {
			_navigationFlow.emit(SettingEffect.NavigateToNickname)
		}
	}

	fun showOpinion() {
		viewModelScope.launch {
			_navigationFlow.emit(SettingEffect.NavigateToOpinion)
		}
	}

	fun showInquiry() {
		viewModelScope.launch {
			_navigationFlow.emit(SettingEffect.NavigateToInquiry)
		}
	}

	fun showPrivacyPolicy() {
		viewModelScope.launch {
			_navigationFlow.emit(SettingEffect.NavigateToPrivacyPolicy)
		}
	}

	fun showTermsOfService() {
		viewModelScope.launch {
			_navigationFlow.emit(SettingEffect.NavigateToTermsOfService)
		}
	}

	fun dismissDialog() {
		viewModelScope.launch {
			_uiState.value = SettingUiState(
				user = _uiState.value.user,
				appInfo = _uiState.value.appInfo,
				status = SettingUiState.Status.Idle,
			)
		}
	}

	fun tryLogout() {
		Timber.e("Logout warning dialog shown")
		viewModelScope.launch {
			_uiState.value = SettingUiState(
				user = _uiState.value.user,
				appInfo = _uiState.value.appInfo,
				status = SettingUiState.Status.LogoutWarning,
			)
		}
	}

	fun logout() {
		Timber.e("Logout initiated")
		viewModelScope.launch {
			when (val result = logoutUseCase()) {
				is BrakeResult.Success -> {
					when (val dest = result.data) {
						is Destination.Login -> {
							firebaseAnalytics.logEvent("app_logout") {
								param(FirebaseAnalytics.Param.METHOD, "user_logout")
							}
							_navigationFlow.emit(SettingEffect.NavigateToLogin)
						}

						else -> {
							Timber.e("Logout failed with destination: $dest")
						}
					}
				}

				is BrakeResult.Error -> {
					_snackBarFlow.emit(
						SnackBarState.Error(
							uiString = UiString.ResourceString(R.string.setting_snackbar_logout_error),
						),
					)
				}
			}
		}
	}

	fun tryDeleteAccount() {
		viewModelScope.launch {
			_uiState.value = SettingUiState(
				user = _uiState.value.user,
				appInfo = _uiState.value.appInfo,
				status = SettingUiState.Status.DeleteWarning,
			)
		}
	}

	fun deleteAccount() {
		_uiState.value = _uiState.value.let {
			SettingUiState(
				user = it.user,
				appInfo = it.appInfo,
				status = SettingUiState.Status.DeletingAccount,
			)
		}
		deleteJob = viewModelScope.launch {
			when (val result = deleteAccountUseCase()) {
				is BrakeResult.Success -> {
					if (result.data is Destination.Login) {
						// 로딩창 먼저 제거 후 스낵바 띄우고 화면 이동: 유저 사용성 증가
						googleAuthManager.signOutGoogleAuth()
						_uiState.value = SettingUiState(
							user = _uiState.value.user,
							appInfo = _uiState.value.appInfo,
							status = SettingUiState.Status.Loaded,
						)
						_snackBarFlow.emit(
							SnackBarState.Success(
								uiString = UiString.ResourceString(R.string.setting_snackbar_delete_success),
							),
						)
						firebaseAnalytics.logEvent("app_delete_account") {
							param(FirebaseAnalytics.Param.METHOD, "user_delete")
						}
						_navigationFlow.emit(SettingEffect.NavigateToLogin)
					} else {
						_uiState.value = SettingUiState(
							user = _uiState.value.user,
							appInfo = _uiState.value.appInfo,
							status = SettingUiState.Status.Idle,
						)
					}
				}

				is BrakeResult.Error -> {
					_uiState.value = SettingUiState(
						user = _uiState.value.user,
						appInfo = _uiState.value.appInfo,
						status = SettingUiState.Status.Loaded,
					)
					_snackBarFlow.emit(
						SnackBarState.Error(
							uiString = UiString.ResourceString(R.string.setting_snackbar_delete_error),
						),
					)
				}
			}
		}
	}

	fun cancelDeletingAccount() {
		deleteJob?.run {
			cancel()
			_uiState.value = _uiState.value.let {
				SettingUiState(
					user = it.user,
					appInfo = it.appInfo,
					status = SettingUiState.Status.Idle,
				)
			}
			viewModelScope.launch {
				_snackBarFlow.emit(
					SnackBarState.Error(
						uiString = UiString.ResourceString(R.string.setting_snackbar_delete_cancel),
					),
				)
			}
		}
	}
}
