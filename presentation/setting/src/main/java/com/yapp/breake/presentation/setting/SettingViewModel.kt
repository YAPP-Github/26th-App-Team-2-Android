package com.yapp.breake.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.auth.google.GoogleAuthManager
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.core.ui.SnackBarState
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.DeleteAccountUseCase
import com.yapp.breake.domain.usecase.GetNicknameUseCase
import com.yapp.breake.domain.usecase.LogoutUseCase
import com.yapp.breake.presentation.setting.model.SettingEffect
import com.yapp.breake.presentation.setting.model.SettingUiState
import com.yapp.breake.presentation.setting.model.SettingUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

	private val _uiState = MutableStateFlow<SettingUiState>(SettingUiState.SettingIdle())
	val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<SettingEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	init {
		viewModelScope.launch {
			getNicknameUseCase({}).collect { nickname ->
				_uiState.update {
					SettingUiState.SettingLoaded(
						user = SettingUser(
							imageUrl = null,
							name = nickname,
						),
						appInfo = _uiState.value.appInfo,
					)
				}
			}
		}
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
			_uiState.value = SettingUiState.SettingIdle(
				user = _uiState.value.user,
				appInfo = _uiState.value.appInfo,
			)
		}
	}

	fun tryLogout() {
		Timber.e("Logout warning dialog shown")
		viewModelScope.launch {
			_uiState.value = SettingUiState.SettingLogoutWarning(
				user = _uiState.value.user,
				appInfo = _uiState.value.appInfo,
			)
		}
	}

	fun logout() {
		Timber.e("Logout initiated")
		viewModelScope.launch {
			val dest = logoutUseCase(
				onError = {
					_snackBarFlow.emit(
						SnackBarState.Error(
							uiString = UiString.ResourceString(R.string.setting_snackbar_logout_error),
						),
					)
				},
			)
			if (dest is Destination.Login) {
				firebaseAnalytics.logEvent("app_logout") {
					param(FirebaseAnalytics.Param.METHOD, "user_logout")
				}
				_navigationFlow.emit(SettingEffect.NavigateToLogin)
			} else if (dest is Destination.PermissionOrHome) {
				Timber.e("Logout failed with destination: $dest")
			} else {
				Timber.e("Logout failed with destination: $dest")
			}
		}
	}

	fun tryDeleteAccount() {
		viewModelScope.launch {
			_uiState.value = SettingUiState.SettingDeleteWarning(
				user = _uiState.value.user,
				appInfo = _uiState.value.appInfo,
			)
		}
	}

	fun deleteAccount() {
		_uiState.value = _uiState.value.let {
			SettingUiState.SettingDeletingAccount(
				user = it.user,
				appInfo = it.appInfo,
			)
		}
		deleteJob = viewModelScope.launch {
			val dest = deleteAccountUseCase(
				onError = {
					_uiState.value = SettingUiState.SettingLoaded(
						user = _uiState.value.user,
						appInfo = _uiState.value.appInfo,
					)
					_snackBarFlow.emit(
						SnackBarState.Error(
							uiString = UiString.ResourceString(R.string.setting_snackbar_delete_error),
						),
					)
				},
			)
			if (dest is Destination.Login) {
				// 로딩창 먼저 제거 후 스낵바 띄우고 화면 이동: 유저 사용성 증가
				googleAuthManager.signOutGoogleAuth()
				_uiState.value = SettingUiState.SettingLoaded(
					user = _uiState.value.user,
					appInfo = _uiState.value.appInfo,
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
			}
		}
	}

	fun cancelDeletingAccount() {
		deleteJob?.run {
			cancel()
			_uiState.value = _uiState.value.let {
				SettingUiState.SettingIdle(
					user = it.user,
					appInfo = it.appInfo,
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
