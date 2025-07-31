package com.yapp.breake.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import com.yapp.breake.presentation.signup.model.SignupEffect
import com.yapp.breake.presentation.signup.model.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
	private val updateNicknameUseCase: UpdateNicknameUseCase,
) : ViewModel() {
	private var updateJob: Job? = null

	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.SignupIdle(""))
	val uiState = _uiState.asStateFlow()

	private val _navigationFlow = MutableSharedFlow<SignupEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun onBackPressed() {
		viewModelScope.launch {
			_navigationFlow.emit(SignupEffect.NavigateToBack)
		}
	}

	fun onNameType(name: String) {
		_uiState.value = SignupUiState.SignupIdle(name)
	}

	fun onNameSubmit(name: String) {
		_uiState.value = SignupUiState.SignupNameRegistering(name)
		updateJob = viewModelScope.launch {
			runCatching {
				updateNicknameUseCase(
					nickname = name,
					onError = {
						_snackBarFlow.emit(UiString.ResourceString(R.string.signup_snackbar_register_error))
						_uiState.value = SignupUiState.SignupIdle(name)
					},
					onSuccess = {
						_uiState.value = SignupUiState.SignupIdle(name)
						_navigationFlow.emit(SignupEffect.NavigateToOnboarding)
					},
				)
			}.onFailure {
				Timber.e(it, "닉네임 업데이트 중 에러 발생")
			}
		}
	}

	fun cancelNameSubmit() {
		updateJob?.run {
			cancel()
			_uiState.value = SignupUiState.SignupIdle(_uiState.value.name)
		}
	}
}
