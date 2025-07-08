package com.yapp.breake.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yapp.breake.presentation.home.navigation.homeNavGraph
import com.yapp.breake.presentation.home.navigation.navigateHome
import com.yapp.breake.presentation.login.navigation.loginNavGraph
import com.yapp.breake.presentation.onboarding.navigation.navigateOnboarding
import com.yapp.breake.presentation.onboarding.navigation.onboardingNavGraph
import com.yapp.breake.presentation.report.navigation.reportNavGraph
import com.yapp.breake.presentation.setting.navigation.settingNavGraph
import com.yapp.breake.presentation.signup.navigation.navigateSignup
import com.yapp.breake.presentation.signup.navigation.signupNavGraph

@Composable
internal fun MainNavHost(
	navigator: MainNavigator,
	padding: PaddingValues,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
	onChangeDarkTheme: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	val navController = navigator.navController

	NavHost(
		modifier = modifier,
		navController = navController,
		startDestination = navigator.startDestination,
	) {
		loginNavGraph(
			navigateToSignup = navController::navigateSignup,
			navigateToHome = {
				navController.navigateHome(shouldClearBackstack = true)
			},
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
		signupNavGraph(
			navigateToLogin = navController::navigateOnboarding,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
		onboardingNavGraph(
			navigateToHome = {
				navController.navigateHome(shouldClearBackstack = true)
			},
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
		reportNavGraph(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
		homeNavGraph(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
		settingNavGraph(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
			onChangeDarkTheme = onChangeDarkTheme,
		)
	}
}
