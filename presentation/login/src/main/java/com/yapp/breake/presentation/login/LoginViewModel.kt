package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.auth.KakaoAuthSDK
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LoginViewModel @Inject constructor(
	private val kakaoAuthSDK: KakaoAuthSDK,
	private val loginUseCase: LoginUseCase,
) : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<LoginEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun loginWithKakao(context: Context) {
		viewModelScope.launch {
			kakaoAuthSDK.login(context).onSuccess { kakaoAccessToken ->
				loginUseCase(kakaoAccessToken.value) { throwable ->
					_errorFlow.emit(throwable)
					return@loginUseCase
				}.firstOrNull { result ->
					when (result) {
						UserTokenStatus.ACTIVE -> {
							_navigationFlow.emit(LoginEffect.NavigateToHome)
						}

						UserTokenStatus.HALF_SIGNUP -> {
							_navigationFlow.emit(LoginEffect.NavigateToSignup)
						}

						UserTokenStatus.INACTIVE -> {
							_errorFlow.emit(Throwable("서버에서 사용자 정보를 찾을 수 없습니다"))
						}
					}
					true
				}
			}.onFailure { error ->
				_errorFlow.emit(error)
			}
		}
	}
}
