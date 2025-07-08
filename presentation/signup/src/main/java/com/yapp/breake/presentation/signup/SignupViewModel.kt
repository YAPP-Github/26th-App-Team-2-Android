package com.yapp.breake.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class SignupViewModel @Inject constructor() : ViewModel() {

	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.SignupIdle(""))
	val uiState = _uiState.asStateFlow()

	fun onNameType(name: String) {
		viewModelScope.launch {
			Timber.d("onNameType: $name")
			_uiState.value = SignupUiState.SignupTypedName(name)
		}
	}
}
