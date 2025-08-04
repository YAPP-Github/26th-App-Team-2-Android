package com.yapp.breake.presentation.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.yapp.breake.presentation.feeback.inquiry.navigation.inquiryNavGraph
import com.yapp.breake.presentation.feeback.opinion.navigation.opinionNavGraph
import com.yapp.breake.presentation.home.navigation.homeNavGraph
import com.yapp.breake.presentation.legal.navigation.legalNavGraph
import com.yapp.breake.presentation.login.navigation.loginNavGraph
import com.yapp.breake.presentation.nickname.navigation.nicknameNavGraph
import com.yapp.breake.presentation.onboarding.navigation.onboardingNavGraph
import com.yapp.breake.presentation.permission.navigation.permissionNavGraph
import com.yapp.breake.presentation.registry.navigation.registryNavGraph
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
