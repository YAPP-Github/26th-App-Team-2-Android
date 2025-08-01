package com.yapp.breake.presentation.legal.privacy

import androidx.lifecycle.ViewModel
import com.yapp.breake.presentation.legal.privacy.model.PrivacyUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PrivacyViewModel @Inject constructor() : ViewModel() {
	private val _uiState = MutableStateFlow<PrivacyUiState>(PrivacyUiState.PrivacyIdle)
	val uiState = _uiState.asStateFlow()
}
