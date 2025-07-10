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
import kotlinx.coroutines.launch
import timber.log.Timber
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
				loginUseCase.invoke(kakaoAccessToken.value).collect { userTokenStatus ->
					when (userTokenStatus) {
						UserTokenStatus.ACTIVE -> {
							Timber.d("Login successful with active user token.")
							_navigationFlow.emit(LoginEffect.NavigateToHome)
						}

						UserTokenStatus.HALF_SIGNUP -> {
							Timber.d("Login successful with half-signed up user token.")
							_navigationFlow.emit(LoginEffect.NavigateToSignup)
						}

						UserTokenStatus.INACTIVE -> {
							_errorFlow.emit(Throwable("카카오 계정에 문제가 있습니다."))
						}
					}
				}
			}.onFailure { error ->
				_errorFlow.emit(error)
			}
		}
	}
}
