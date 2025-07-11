package com.yapp.breake.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import com.yapp.breake.presentation.signup.model.SignupEffect
import com.yapp.breake.presentation.signup.model.SignupUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
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
		_uiState.value = SignupUiState.SignupTypedName(name)
	}

	fun onNameSubmit(name: String) {
		viewModelScope.launch {
			updateNicknameUseCase(name)
				.catch { e ->
					_errorFlow.emit(e)
				}
				.firstOrNull { result ->
					when (result) {
						is ResponseResult.Success -> {
							_navigationFlow.emit(SignupEffect.NavigateToOnboarding)
						}
						is ResponseResult.Error -> {
							_errorFlow.emit(Exception(result.message))
						}
						is ResponseResult.Exception -> {
							_errorFlow.emit(result.exception)
						}
					}
					true
				}
		}
	}
}
