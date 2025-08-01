package com.yapp.breake.presentation.legal.terms

import androidx.lifecycle.ViewModel
import com.yapp.breake.presentation.legal.terms.model.TermsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TermsViewModel @Inject constructor() : ViewModel() {
	private val _uiState = MutableStateFlow<TermsUiState>(TermsUiState.PrivacyIdle)
	val uiState = _uiState.asStateFlow()
}
