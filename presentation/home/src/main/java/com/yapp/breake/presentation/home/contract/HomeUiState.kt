package com.yapp.breake.presentation.home.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.yapp.breake.core.model.app.AppGroup

@Stable
internal sealed interface HomeUiState {

	@Immutable
	data object Loading : HomeUiState

	@Immutable
	data object Nothing : HomeUiState

	@Immutable
	data class GroupList(val appGroups: List<AppGroup>) : HomeUiState

	@Immutable
	data class Blocking(val appGroup: AppGroup) : HomeUiState

	@Immutable
	data class Using(val appGroup: AppGroup) : HomeUiState
}
