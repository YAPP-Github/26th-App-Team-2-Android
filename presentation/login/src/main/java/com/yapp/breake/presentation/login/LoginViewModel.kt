package com.yapp.breake.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginEffect
import com.yapp.breake.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
	private val loginUseCase: LoginUseCase,
) : ViewModel() {

	private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.LoginIdle)
	val uiState = _uiState.asStateFlow()

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<LoginEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	private var loginJob: Job? = null

	fun loginWithKakao() {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginOnWebView
		}
	}

	fun loginCancel() {
		viewModelScope.launch {
			loginJob?.cancel()
			_uiState.value = LoginUiState.LoginIdle
		}
	}

	fun authSuccess(authCode: String) {
		loginJob?.cancel()
		loginJob = viewModelScope.launch {
			_uiState.value = LoginUiState.LoginLoading
			loginUseCase(authCode) { throwable ->
				_uiState.value = LoginUiState.LoginIdle
				_errorFlow.emit(throwable)
				return@loginUseCase
			}.firstOrNull { result ->
				when (result) {
					UserTokenStatus.ACTIVE -> {
						_uiState.value = LoginUiState.LoginIdle
						_navigationFlow.emit(LoginEffect.NavigateToHome)
					}

					UserTokenStatus.HALF_SIGNUP -> {
						_uiState.value = LoginUiState.LoginIdle
						_navigationFlow.emit(LoginEffect.NavigateToSignup)
					}

					UserTokenStatus.INACTIVE -> {
						_uiState.value = LoginUiState.LoginIdle
						_errorFlow.emit(Throwable("서버에서 사용자 정보를 찾을 수 없습니다"))
					}
				}
				true
			}
		}
	}

	fun loginFailure(throwable: Throwable) {
		viewModelScope.launch {
			_uiState.value = LoginUiState.LoginIdle
			_errorFlow.emit(throwable)
		}
	}
}
