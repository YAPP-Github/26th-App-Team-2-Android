package com.yapp.breake.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import com.yapp.breake.presentation.signup.model.SignupEffect
import com.yapp.breake.presentation.signup.model.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
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

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

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
		Timber.d("onNameType: $name")
		_uiState.value = SignupUiState.SignupTypedName(name)
	}

	fun onNameSubmit(name: String) {
		viewModelScope.launch {
			runCatching {
				updateNicknameUseCase(name)
			}.onSuccess {
				Timber.d("Nickname updated successfully")
				_navigationFlow.emit(SignupEffect.NavigateToOnboarding)
			}.onFailure { error ->
				Timber.e(error, "Failed to update nickname :${error.message}")
				_errorFlow.emit(error)
			}
		}
	}
}
