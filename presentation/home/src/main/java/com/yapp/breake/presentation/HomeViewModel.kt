package com.yapp.breake.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.yapp.breake.core.model.app.AppGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel()

@Stable
sealed interface HomeUiState {
	data object Nothing : HomeUiState
	data class GroupList(val appGroups: List<AppGroup>) : HomeUiState
	data object Blocking : HomeUiState
	data object Using : HomeUiState
}
