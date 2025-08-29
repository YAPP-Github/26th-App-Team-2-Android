package com.yapp.breake.presentation.home.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.yapp.breake.core.model.app.AppGroup
import kotlinx.collections.immutable.PersistentList

@Stable
internal sealed interface HomeUiState {

	@Immutable
	data object Loading : HomeUiState

	@Immutable
	data object Nothing : HomeUiState

	@Immutable
	data class GroupList(val appGroups: PersistentList<AppGroup>) : HomeUiState

	@Immutable
	data class Ticking(val appGroups: PersistentList<AppGroup>) : HomeUiState
}
