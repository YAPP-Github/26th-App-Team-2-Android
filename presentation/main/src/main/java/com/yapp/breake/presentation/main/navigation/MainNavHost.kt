package com.yapp.breake.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yapp.breake.presentation.home.navigation.homeNavGraph
import com.yapp.breake.presentation.login.navigation.loginNavGraph
import com.yapp.breake.presentation.onboarding.navigation.onboardingNavGraph
import com.yapp.breake.presentation.report.navigation.reportNavGraph
import com.yapp.breake.presentation.setting.navigation.settingNavGraph
import com.yapp.breake.presentation.signup.navigation.signupNavGraph

@Composable
internal fun MainNavHost(
	navigator: MainNavigator,
	padding: PaddingValues,
	onChangeDarkTheme: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val navController = navigator.navController

	NavHost(
		modifier = modifier,
		navController = navController,
		startDestination = navigator.startDestination,
	) {
		loginNavGraph()
		signupNavGraph()
		onboardingNavGraph()
		reportNavGraph(padding = padding)
		homeNavGraph(padding = padding)
		settingNavGraph(
			padding = padding,
			onChangeDarkTheme = onChangeDarkTheme,
		)
	}
}
