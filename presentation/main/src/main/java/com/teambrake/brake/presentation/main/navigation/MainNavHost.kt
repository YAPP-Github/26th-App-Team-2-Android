package com.teambrake.brake.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.teambrake.brake.presentation.feeback.inquiry.navigation.inquiryNavGraph
import com.teambrake.brake.presentation.feeback.opinion.navigation.opinionNavGraph
import com.teambrake.brake.presentation.home.navigation.homeNavGraph
import com.teambrake.brake.presentation.legal.navigation.legalNavGraph
import com.teambrake.brake.presentation.login.navigation.loginNavGraph
import com.teambrake.brake.presentation.nickname.navigation.nicknameNavGraph
import com.teambrake.brake.presentation.onboarding.navigation.onboardingNavGraph
import com.teambrake.brake.presentation.permission.navigation.permissionNavGraph
import com.teambrake.brake.presentation.registry.navigation.registryNavGraph
import com.teambrake.brake.presentation.report.navigation.reportNavGraph
import com.teambrake.brake.presentation.setting.navigation.settingNavGraph
import com.teambrake.brake.presentation.signup.navigation.signupNavGraph

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
		legalNavGraph()
		permissionNavGraph()
		reportNavGraph(padding = padding)
		homeNavGraph(padding = padding)
		registryNavGraph()
		settingNavGraph(
			padding = padding,
			onChangeDarkTheme = onChangeDarkTheme,
		)
		nicknameNavGraph()
		inquiryNavGraph()
		opinionNavGraph()
	}
}
