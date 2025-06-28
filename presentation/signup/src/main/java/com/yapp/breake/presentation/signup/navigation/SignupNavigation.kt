package com.yapp.breake.presentation.signup.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.InitialRoute
import com.yapp.breake.presentation.signup.SignupRoute

fun NavController.navigateSignup(navOptions: NavOptions? = null) {
	navigate(InitialRoute.SignUp, navOptions)
}

fun NavGraphBuilder.signupNavGraph(
	navigateToLogin: () -> Unit,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit,
) {
	composable<InitialRoute.SignUp> {
		SignupRoute(
			navigateToLogin = navigateToLogin,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
	}
}
