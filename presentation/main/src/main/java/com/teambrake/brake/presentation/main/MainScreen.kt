package com.teambrake.brake.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teambrake.brake.core.designsystem.theme.DynamicPaddingsProvider
import com.teambrake.brake.core.designsystem.theme.LocalDynamicPaddings
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.presentation.main.component.BrakeSnackbar
import com.teambrake.brake.presentation.main.component.BrakeSnackbarHostState
import com.teambrake.brake.presentation.main.component.BrakeSnackbarHost
import com.teambrake.brake.presentation.main.navigation.MainBottomNavBar
import com.teambrake.brake.presentation.main.navigation.MainNavHost
import com.teambrake.brake.presentation.main.navigation.MainNavigator
import com.teambrake.brake.presentation.main.navigation.MainTab
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

	// 바텀 패딩 조정 용도 (스낵바 높이 위치 및 하단 네비게이션 바 상호작용)
	val dynamicPaddingsProvider = remember { DynamicPaddingsProvider() }
	val density = LocalDensity.current

	Scaffold(
		modifier = modifier,
		content = { padding ->
			Box(
				modifier = Modifier.fillMaxSize(),
			) {
				CompositionLocalProvider(
					LocalDynamicPaddings provides dynamicPaddingsProvider,
				) {
					MainNavHost(
						navigator = navigator,
						padding = padding,
						onChangeDarkTheme = onChangeDarkTheme,
					)
				}

				Box(
					modifier = Modifier
						.align(Alignment.BottomCenter)
						.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
						) { /* 터치 이벤트 가로채기 */ }
						.onGloballyPositioned { coordinates ->
							with(density) {
								dynamicPaddingsProvider.updateBottomNavHeight(
									coordinates.size.height.toDp() + 12.dp,
								)
							}
						}
						.fillMaxWidth()
						.wrapContentWidth(Alignment.CenterHorizontally)
						.navigationBarsPadding()
						.padding(bottom = 34.dp),
				) {
					// AnimatedVisibility 를 사용할 경우, 스낵바의 y 좌표 위치 변동 시 애니메이션 활성화 동안 스낵바의 위치가 튀는 현상 발생
					val route = currentRoute
					if (route is MainTabRoute) {
						MainBottomNavBar(
							modifier = Modifier
								.background(Color.Transparent),
							tabs = MainTab.entries.toPersistentList(),
							currentTab = when (route) {
								is MainTabRoute.Home -> MainTab.HOME
								is MainTabRoute.Report -> MainTab.REPORT
								is MainTabRoute.Setting -> MainTab.SETTING
							},
							onTabSelected = navigator::navigate,
						)
					}
				}
			}
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
						is MainTabRoute -> Modifier.padding(
							bottom = dynamicPaddingsProvider.paddings.bottomNavBarHeight,
						)

						InitialRoute.Login ->
							Modifier
								.navigationBarsPadding()
								.padding(
									bottom = dynamicPaddingsProvider.paddings.twoButtonHeight,
								)

						else ->
							Modifier
								.navigationBarsPadding()
								.padding(
									bottom = dynamicPaddingsProvider.paddings.oneButtonHeight,
								)
					},
				),
			)
		},
	)
}
