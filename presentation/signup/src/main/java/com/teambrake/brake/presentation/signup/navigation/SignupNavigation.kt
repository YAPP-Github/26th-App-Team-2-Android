package com.teambrake.brake.presentation.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.presentation.signup.SignupRoute
import com.teambrake.brake.core.navigation.route.InitialRoute

fun NavController.navigateToSignup(navOptions: NavOptions? = null) {
	navigate(InitialRoute.SignUp, navOptions)
}

fun NavGraphBuilder.signupNavGraph() {
	composable<InitialRoute.SignUp> {
		SignupRoute()
	}
}
