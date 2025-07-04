package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.auth.KakaoAuthSDK
import com.yapp.breake.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
	private val kakaoAuthSDK: KakaoAuthSDK,
) : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _uiState = MutableSharedFlow<LoginUiState>()
	val uiState = _uiState.asSharedFlow()

	fun loginWithKakao(context: Context) {
		viewModelScope.launch {
			kakaoAuthSDK.login(context).onSuccess { accessToken ->
				Timber.d("Kakao 로그인 성공")
				_uiState.emit(LoginUiState.LoginAsRegistered)
			}.onFailure { error ->
				_errorFlow.tryEmit(error)
			}
		}
	}
}
