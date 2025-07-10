package com.yapp.breake.presentation.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.presentation.signup.SignupRoute

fun NavController.navigateToSignup(navOptions: NavOptions? = null) {
	navigate(InitialRoute.SignUp, navOptions)
}

fun NavGraphBuilder.signupNavGraph() {
	composable<InitialRoute.SignUp> {
		SignupRoute()
	}
}
