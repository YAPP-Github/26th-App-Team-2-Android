package com.yapp.breake.presentation.main

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.yapp.breake.presentation.main.navigation.MainBottomNavBar
import com.yapp.breake.presentation.main.navigation.MainNavHost
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.MainTab
import com.yapp.breake.presentation.main.navigation.rememberMainNavigator
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@Composable
internal fun MainScreen(
	navigator: MainNavigator = rememberMainNavigator(),
	coroutineScope: CoroutineScope = rememberCoroutineScope(),
	onChangeDarkTheme: (Boolean) -> Unit,
) {
	val snackBarHostState = remember { SnackbarHostState() }

	val localResources = LocalContext.current.resources
	val onShowErrorSnackBar: (throwable: Throwable?) -> Unit = { throwable ->
		coroutineScope.launch {
			snackBarHostState.showSnackbar(
				when (throwable) {
					is UnknownHostException -> localResources.getString(R.string.network_error)
					else -> localResources.getString(R.string.unknown_error)
				},
			)
		}
	}

	MainScreenContent(
		navigator = navigator,
		onShowErrorSnackBar = onShowErrorSnackBar,
		onChangeDarkTheme = onChangeDarkTheme,
		snackBarHostState = snackBarHostState,
	)
}

@Composable
private fun MainScreenContent(
	navigator: MainNavigator,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
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
				onShowErrorSnackBar = onShowErrorSnackBar,
				onChangeDarkTheme = onChangeDarkTheme,
			)
		},
		bottomBar = {
			MainBottomNavBar(
				modifier = Modifier
					.navigationBarsPadding()
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
