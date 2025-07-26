package com.yapp.breake.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.usecase.DeleteAccountUseCase
import com.yapp.breake.domain.usecase.LogoutUseCase
import com.yapp.breake.presentation.setting.model.SettingEffect
import com.yapp.breake.presentation.setting.model.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
	private val deleteAccountUseCase: DeleteAccountUseCase,
	private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _uiState = MutableStateFlow<SettingUiState>(SettingUiState.SettingIdle())
	val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<SettingEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun logout() {
		Timber.e("Logout initiated")
		viewModelScope.launch {
			val dest = logoutUseCase(
				onError = {
					_errorFlow.emit(it)
				},
			)
			if (dest is Destination.Login) {
				_navigationFlow.emit(SettingEffect.NavigateToLogin)
			} else if (dest is Destination.PermissionOrHome) {
				Timber.e("Logout failed with destination: $dest")
			} else {
				Timber.e("Logout failed with destination: $dest")
			}
		}
	}

	fun deleteAccount() {
		viewModelScope.launch {
			val dest = deleteAccountUseCase(
				onError = {
					_errorFlow.emit(it)
				},
			)
			if (dest is Destination.Login) {
				_navigationFlow.emit(SettingEffect.NavigateToLogin)
			}
		}
	}
}
