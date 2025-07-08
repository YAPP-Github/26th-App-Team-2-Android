package com.yapp.breake.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.auth.KakaoAuthSDK
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.presentation.login.model.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
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

	private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.LoginIdle)
	val uiState = _uiState.asSharedFlow()

	fun loginWithKakao(context: Context) {
		viewModelScope.launch {
			kakaoAuthSDK.login(context).onSuccess { kakaoAccessToken ->
				loginUseCase.invoke(kakaoAccessToken.value).collect { userTokenStatus ->
					when (userTokenStatus) {
						UserTokenStatus.ACTIVE -> {
							Timber.d("Login successful with active user token.")
							_uiState.value = LoginUiState.LoginAsRegistered
						}
						UserTokenStatus.HALF_SIGNUP -> {
							Timber.d("Login successful with half-signed up user token.")
							_uiState.value = LoginUiState.LoginAsNewUser
						}
						UserTokenStatus.INACTIVE -> {
							Timber.d("Login failed with inactive user token.")
							_uiState.value = LoginUiState.LoginInvalidUser
						}
					}
				}
			}.onFailure { error ->
				_errorFlow.tryEmit(error)
			}
		}
	}
}
