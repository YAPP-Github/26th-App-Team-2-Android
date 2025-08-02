package com.yapp.breake.presentation.registry

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.designsystem.theme.LocalPadding
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.registry.component.GroupDeletionWarningDialog
import com.yapp.breake.presentation.registry.model.RegistryNavState
import com.yapp.breake.presentation.registry.model.RegistryModalState
import com.yapp.breake.presentation.registry.model.RegistrySnackBarState
import com.yapp.breake.presentation.registry.model.RegistryUiState
import com.yapp.breake.presentation.registry.screen.AppRegistryScreen
import com.yapp.breake.presentation.registry.screen.GroupRegistryScreen

@Composable
fun RegistryRoute(
	viewModel: RegistryViewModel,
) {
	val padding = LocalPadding.current.screenPaddingHorizontal
	val registryUiState by viewModel.registryUiState.collectAsStateWithLifecycle()
	val registryModalState by viewModel.registryModalFlow.collectAsStateWithLifecycle()
	val focusManager = LocalFocusManager.current
	val context = LocalContext.current
	val navAction = LocalNavigatorAction.current
	val mainAction = LocalMainAction.current
	val density = LocalDensity.current
	val imeVisible = WindowInsets.ime.getBottom(density) > 0
	var prevVisible by remember { mutableStateOf(imeVisible) }

	// 키보드가 사라졌을 때 포커스를 해제
	SideEffect {
		if (prevVisible && !imeVisible) {
			focusManager.clearFocus()
		}
		prevVisible = imeVisible
	}

	LaunchedEffect(true) {
		viewModel.navigationFlow.collect { effect ->
			when (effect) {
				is RegistryNavState.NavigateToHome -> navAction.popBackStack()
			}
		}
	}

	LaunchedEffect(true) {
		viewModel.snackBarFlow.collect {
			when (it) {
				is RegistrySnackBarState.Success -> {
					mainAction.onShowSuccessMessage(
						message = it.uiString.asString(context = context),
					)
				}
				is RegistrySnackBarState.Error -> {
					mainAction.onShowErrorMessage(
						message = it.uiString.asString(context = context),
					)
				}
			}
		}
	}

	when (registryModalState) {
		is RegistryModalState.Idle -> {}
		RegistryModalState.ShowGroupDeletionWarning -> {
			GroupDeletionWarningDialog(
				onDismissRequest = viewModel::dismissModal,
				onConfirm = viewModel::removeGroup,
			)
		}
	}

	when (registryUiState) {
		is RegistryUiState.Group.Initial, is RegistryUiState.Group.Updated -> {
			GroupRegistryScreen(
				padding = padding,
				registryUiState = registryUiState,
				focusManager = focusManager,
				onGroupNameChange = viewModel::updateGroupName,
				onStartSelectingApps = viewModel::startSelectingApps,
				onRemoveApp = viewModel::removeSelectedApp,
				onRemoveGroup = viewModel::tryRemoveGroup,
				onRegisterGroup = viewModel::createNewGroup,
				onBackClick = viewModel::cancelCreatingNewGroup,
			)
		}

		is RegistryUiState.App -> {
			AppRegistryScreen(
				padding = padding,
				registryUiState = registryUiState as RegistryUiState.App,
				lazyColumnIndexFlow = viewModel.lazyColumnIndexFlow,
				focusManager = focusManager,
				onSearchTextChange = viewModel::updateSearchingText,
				onSearchApp = { viewModel.searchApp(shouldShowNotFound = true) },
				onSelectApp = viewModel::selectApp,
				onBackClick = viewModel::cancelSelectingApps,
				onRegisterApps = viewModel::completeSelectingApps,
			)
		}
	}
}
