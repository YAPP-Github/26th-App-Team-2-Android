package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.auth.KakaoAuthSDK
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
	private val kakaoAuthSDK: KakaoAuthSDK,
	private val loginUseCase: LoginUseCase,
) : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _uiState = MutableSharedFlow<LoginUiState>()
	val uiState = _uiState.asSharedFlow()

	fun loginWithKakao(context: Context) {
		viewModelScope.launch {
			kakaoAuthSDK.login(context).onSuccess { kakaoAccessToken ->
				loginUseCase.invoke(kakaoAccessToken.value).collect { serverToken ->
					// TODO: 추가적인 수정 필요
					if (serverToken.accessToken != null) {
						_uiState.emit(LoginUiState.LoginAsRegistered)
					}
				}
			}.onFailure { error ->
				_errorFlow.tryEmit(error)
			}
		}
	}
}
