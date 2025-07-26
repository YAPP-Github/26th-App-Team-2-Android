package com.yapp.breake.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.theme.BackgroundGradient
import com.yapp.breake.presentation.HomeUiState
import com.yapp.breake.presentation.HomeViewModel
import com.yapp.breake.presentation.home.screen.BlockingScreen
import com.yapp.breake.presentation.home.screen.ListScreen
import com.yapp.breake.presentation.home.screen.NothingScreen
import com.yapp.breake.presentation.home.screen.UsingScreen

@Composable
internal fun HomeRoute(
	padding: PaddingValues,
	viewModel: HomeViewModel = hiltViewModel(),
) {
	BaseScaffold {
		Box(
			modifier = Modifier.fillMaxSize(),
		) {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(360.dp)
					.background(brush = BackgroundGradient),
			)
//			HomeContent(
//				homeUiState = viewModel.homeUiState,
//				modifier = Modifier.fillMaxSize(),
//			)
		}
	}
}

@Composable
private fun HomeContent(
	homeUiState: HomeUiState,
) {
	when (homeUiState) {
		HomeUiState.Nothing -> {
			NothingScreen(
				onAddClick = { /* TODO: Handle add click */ },
			)
		}

		is HomeUiState.GroupList -> {
			ListScreen(
				appGroups = homeUiState.appGroups,
				onAppGroupClick = { },
			)
		}

		is HomeUiState.Blocking -> {
			BlockingScreen(
				appGroup = homeUiState.appGroup,
				onEditClick = { /* TODO: Handle edit click */ },
			)
		}

		is HomeUiState.Using -> {
			UsingScreen(
				appGroup = homeUiState.appGroup,
				onEditClick = { /* TODO: Handle edit click */ },
				onStopClick = { /* TODO: Handle stop click */ },
			)
		}
	}
}
