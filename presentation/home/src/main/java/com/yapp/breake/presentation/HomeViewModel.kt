package com.yapp.breake.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel()

@Stable
sealed interface HomeUiState {
	data object Nothing : HomeUiState
	data object List : HomeUiState
	data object Blocking : HomeUiState
	data object Using : HomeUiState
}
