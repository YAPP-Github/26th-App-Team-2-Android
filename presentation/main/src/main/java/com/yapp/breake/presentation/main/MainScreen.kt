package com.yapp.breake.presentation.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.presentation.main.component.BrakeSnackbar
import com.yapp.breake.presentation.main.component.BrakeSnackbarHostState
import com.yapp.breake.presentation.main.component.BrakeSnackbarHost
import com.yapp.breake.presentation.main.navigation.MainBottomNavBar
import com.yapp.breake.presentation.main.navigation.MainNavHost
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.MainTab
import kotlinx.collections.immutable.toPersistentList

@Composable
internal fun MainScreen(
	navigator: MainNavigator,
	onChangeDarkTheme: (Boolean) -> Unit,
	snackBarHostState: BrakeSnackbarHostState,
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
	snackBarHostState: BrakeSnackbarHostState,
	modifier: Modifier = Modifier,
) {
	// 1. 현재 화면에 따라 실시간 스낵바 위치 조정을 위한 Route 구독
	// 2. MainTabRoute 화면일 때 하단 네비게이션 바를 띄우고, 그 외는 안띄우기 위한 Route 구독
	val currentRoute by navigator.currentRoute.collectAsStateWithLifecycle()

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
					.padding(bottom = 34.dp)
					.wrapContentWidth(Alignment.CenterHorizontally),
				visible = currentRoute is MainTabRoute,
				tabs = MainTab.entries.toPersistentList(),
				currentTab = when (currentRoute) {
					is MainTabRoute.Home -> MainTab.HOME
					is MainTabRoute.Report -> MainTab.REPORT
					is MainTabRoute.Setting -> MainTab.SETTING
					else -> null
				},
				onTabSelected = navigator::navigate,
			)
		},
		contentWindowInsets = WindowInsets(0.dp),
		snackbarHost = {
			BrakeSnackbarHost(
				hostState = snackBarHostState,
				snackbar = { snackbarData ->
					BrakeSnackbar(
						snackbarData = snackbarData,
					)
				},
				// 현재 화면에 따라 스낵바 y 축 위치 조정
				modifier = Modifier.then(
					when (currentRoute) {
						is MainTabRoute -> Modifier.padding(bottom = 0.dp)

						InitialRoute.Login ->
							Modifier
								.navigationBarsPadding()
								.padding(bottom = 190.dp)

						else ->
							Modifier
								.navigationBarsPadding()
								.padding(bottom = 80.dp)
					},
				),
			)
		},
	)
}
