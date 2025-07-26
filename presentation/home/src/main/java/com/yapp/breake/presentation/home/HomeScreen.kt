package com.yapp.breake.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.theme.BackgroundGradient
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.presentation.HomeEvent
import com.yapp.breake.presentation.HomeModalState
import com.yapp.breake.presentation.HomeUiState
import com.yapp.breake.presentation.HomeViewModel
import com.yapp.breake.presentation.home.component.StopUsingDialog
import com.yapp.breake.presentation.home.screen.BlockingScreen
import com.yapp.breake.presentation.home.screen.ListScreen
import com.yapp.breake.presentation.home.screen.NothingScreen
import com.yapp.breake.presentation.home.screen.UsingScreen

@Composable
internal fun HomeRoute(
	padding: PaddingValues,
	viewModel: HomeViewModel = hiltViewModel(),
) {
	val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
	val homeModalState by viewModel.homeModalState.collectAsStateWithLifecycle()
	val mainAction = LocalMainAction.current
	val context = LocalContext.current

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(padding),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(360.dp)
				.background(brush = BackgroundGradient),
		)
		HomeContent(
			homeUiState = homeUiState,
			viewModel = viewModel,
			onShowAddScreen = {
			},
			onShowEditScreen = { groupId ->
			},
		)
	}

	ModalContent(
		homeModalState = homeModalState,
		viewModel = viewModel,
	)

	LaunchedEffect(Unit) {
		viewModel.homeEvent.collect { event ->
			when (event) {
				is HomeEvent.ShowStopUsingSuccess -> {
					mainAction.onShowToast(
						context.getString(
							R.string.home_stop_using_success,
							event.groupName,
							event.groupName,
						),
					)
				}
			}
		}
	}
}

@Composable
private fun HomeContent(
	homeUiState: HomeUiState,
	viewModel: HomeViewModel,
	onShowAddScreen: () -> Unit,
	onShowEditScreen: (Long) -> Unit,
) {
	when (homeUiState) {
		HomeUiState.Nothing -> {
			NothingScreen(
				onAddClick = onShowAddScreen,
			)
		}

		is HomeUiState.GroupList -> {
			ListScreen(
				appGroups = homeUiState.appGroups,
				onEditClick = {
					onShowEditScreen(it.id)
				},
			)
		}

		is HomeUiState.Blocking -> {
			BlockingScreen(
				appGroup = homeUiState.appGroup,
				onEditClick = {
					onShowEditScreen(homeUiState.appGroup.id)
				},
			)
		}

		is HomeUiState.Using -> {
			UsingScreen(
				appGroup = homeUiState.appGroup,
				onEditClick = {
					onShowEditScreen(homeUiState.appGroup.id)
				},
				onStopClick = {
					viewModel.showStopUsingDialog(homeUiState.appGroup)
				},
			)
		}
	}
}

@Composable
private fun ModalContent(
	homeModalState: HomeModalState,
	viewModel: HomeViewModel,
) {
	when (homeModalState) {
		HomeModalState.Nothing -> {}
		is HomeModalState.StopUsingDialog -> {
			StopUsingDialog(
				onStopUsing = {
					viewModel.stopAppUsing(homeModalState.appGroup)
				},
				onDismissRequest = viewModel::dismiss,
			)
		}
	}
}
