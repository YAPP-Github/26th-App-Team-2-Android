package com.yapp.breake.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.theme.LinerGradient
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.home.component.StopUsingDialog
import com.yapp.breake.presentation.home.contract.HomeEvent
import com.yapp.breake.presentation.home.contract.HomeModalState
import com.yapp.breake.presentation.home.contract.HomeUiState
import com.yapp.breake.presentation.home.screen.ListScreen
import com.yapp.breake.presentation.home.screen.NothingScreen
import com.yapp.breake.presentation.home.screen.TickingScreen

@Composable
internal fun HomeRoute(
	padding: PaddingValues,
	viewModel: HomeViewModel = hiltViewModel(),
) {
	val homeUiState by viewModel.homeUiState.collectAsStateWithLifecycle()
	val homeModalState by viewModel.homeModalState.collectAsStateWithLifecycle()
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current
	val context = LocalContext.current

	val hasRequestedNotificationPermission = remember { mutableStateOf(false) }
	val notificationPermissionLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.RequestPermission(),
	) { _ ->
		hasRequestedNotificationPermission.value = true
	}

	mainAction.OnFinishBackHandler()

	LaunchedEffect(Unit) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			val hasNotificationPermission = ContextCompat.checkSelfPermission(
				context,
				Manifest.permission.POST_NOTIFICATIONS,
			) == PackageManager.PERMISSION_GRANTED

			if (!hasNotificationPermission && !hasRequestedNotificationPermission.value) {
				notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
			}
		}
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(padding),
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.fillMaxHeight(0.4f)
				.background(brush = LinerGradient),
		)
		HomeContent(
			homeUiState = homeUiState,
			viewModel = viewModel,
			onShowAddScreen = viewModel::navigateToRegistry,
			onShowEditScreen = viewModel::navigateToRegistry,
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
					mainAction.onShowSuccessMessage(
						context.getString(
							R.string.home_stop_using_success,
							event.groupName,
							event.groupName,
						),
					)
				}

				is HomeEvent.NavigateToRegistry -> {
					navAction.navigateToRegistry(groupId = event.groupId)
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
	when (val state = homeUiState) {
		HomeUiState.Loading -> {}

		HomeUiState.Nothing -> {
			NothingScreen(
				onAddClick = onShowAddScreen,
			)
		}

		is HomeUiState.GroupList -> {
			ListScreen(
				appGroups = state.appGroups,
				onEditClick = {
					onShowEditScreen(it.id)
				},
				onAddClick = onShowAddScreen,
			)
		}

		is HomeUiState.Ticking -> {
			TickingScreen(
				appGroups = state.appGroups,
				onAddClick = onShowAddScreen,
				onEditClick = {
					onShowEditScreen(it.id)
				},
				onStopClick = viewModel::showStopUsingDialog,
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
					viewModel.dismiss()
					viewModel.stopAppUsing(homeModalState.appGroup)
				},
				onDismissRequest = viewModel::dismiss,
			)
		}
	}
}
