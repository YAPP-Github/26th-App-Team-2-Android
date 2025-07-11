package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.auth.KakaoAuthSDK
import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
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
				loginUseCase(kakaoAccessToken.value)
					.catch { e ->
						_errorFlow.emit(Throwable(e))
						return@catch
					}
					.firstOrNull { result ->
						when (result) {
							is ResponseResult.Success -> {
								when (result.data) {
									UserTokenStatus.ACTIVE -> {
										_navigationFlow.emit(LoginEffect.NavigateToHome)
									}
									UserTokenStatus.HALF_SIGNUP -> {
										_navigationFlow.emit(LoginEffect.NavigateToSignup)
									}
									else -> {}
								}
							}

							is ResponseResult.Error -> {
								_navigationFlow.emit(LoginEffect.NavigateToSignup)
							}

							is ResponseResult.Exception -> {
								_errorFlow.emit(result.exception)
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
