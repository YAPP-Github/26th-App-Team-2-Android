package com.yapp.breake.presentation.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.component.BrakeSnackbar
import com.yapp.breake.presentation.main.navigation.MainBottomNavBar
import com.yapp.breake.presentation.main.navigation.MainNavHost
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.MainTab
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
	navigator: MainNavigator,
	onChangeDarkTheme: (Boolean) -> Unit,
	snackBarHostState: SnackbarHostState,
) {
	MainScreenContent(
		navigator = navigator,
		onChangeDarkTheme = onChangeDarkTheme,
		snackBarHostState = snackBarHostState,
	)
}

@Composable
private fun MainScreenContent(
	navigator: MainNavigator,
	onChangeDarkTheme: (Boolean) -> Unit,
	snackBarHostState: SnackbarHostState,
	modifier: Modifier = Modifier,
) {
	Scaffold(
		modifier = modifier,
		content = { padding ->
			MainNavHost(
				navigator = navigator,
				padding = padding,
				onChangeDarkTheme = onChangeDarkTheme,
			)
		},
		bottomBar = {
			MainBottomNavBar(
				modifier = Modifier
					.navigationBarsPadding()
					.fillMaxWidth()
					.padding(bottom = 50.dp)
					.wrapContentWidth(Alignment.CenterHorizontally),
				visible = navigator.shouldShowBottomBar(),
				tabs = MainTab.entries.toPersistentList(),
				currentTab = navigator.currentTab,
				onTabSelected = navigator::navigate,
			)
		},
		contentWindowInsets = WindowInsets(0.dp),
		snackbarHost = {
			SnackbarHost(
				hostState = snackBarHostState,
				snackbar = { snackbarData ->
					BrakeSnackbar(
						snackbarData = snackbarData,
					)
				},
				modifier = Modifier
					.navigationBarsPadding(),
			)
		},
	)
}
