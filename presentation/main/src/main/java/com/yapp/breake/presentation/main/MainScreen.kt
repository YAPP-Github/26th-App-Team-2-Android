package com.yapp.breake.presentation.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yapp.breake.presentation.main.navigation.MainBottomNavBar
import com.yapp.breake.presentation.main.navigation.MainNavHost
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.MainTab
import com.yapp.breake.presentation.main.navigation.rememberMainNavigator
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
	navigator: MainNavigator = rememberMainNavigator(),
	onChangeDarkTheme: (Boolean) -> Unit,
) {
	val snackBarHostState = remember { SnackbarHostState() }

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
					.padding(start = 48.dp, end = 48.dp, bottom = 50.dp),
				visible = navigator.shouldShowBottomBar(),
				tabs = MainTab.entries.toPersistentList(),
				currentTab = navigator.currentTab,
				onTabSelected = navigator::navigate,
			)
		},
		snackbarHost = { SnackbarHost(snackBarHostState) },
	)
}
