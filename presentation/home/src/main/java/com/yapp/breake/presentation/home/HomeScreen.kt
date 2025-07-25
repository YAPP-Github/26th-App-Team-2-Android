package com.yapp.breake.presentation.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.presentation.HomeUiState
import com.yapp.breake.presentation.HomeViewModel
import com.yapp.breake.presentation.home.screen.NothingScreen

@Composable
internal fun HomeRoute(
	padding: PaddingValues,
	viewModel: HomeViewModel = hiltViewModel(),
) {
	BaseScaffold {

	}

}

@Composable
private fun HomeContent(
	homeUiState: HomeUiState,
	modifier: Modifier = Modifier,
) {
	when (homeUiState) {
		HomeUiState.Nothing -> {
			NothingScreen(
				onAddClick = { /* TODO: Handle add click */ },
			)
		}
		HomeUiState.List -> TODO()
		HomeUiState.Blocking -> {
		}
		HomeUiState.Using -> TODO()
	}
}
