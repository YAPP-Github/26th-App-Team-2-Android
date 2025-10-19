package com.teambrake.brake.presentation.legal.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.legal.privacy.PrivacyRoute
import com.teambrake.brake.presentation.legal.terms.TermsRoute

fun NavController.navigateToPrivacy(navOptions: NavOptions? = null) {
	navigate(SubRoute.Privacy, navOptions)
}

fun NavController.navigateToTerms(navOptions: NavOptions? = null) {
	navigate(SubRoute.Terms, navOptions)
}

fun NavGraphBuilder.legalNavGraph() {
	composable<SubRoute.Privacy> {
		PrivacyRoute()
	}
	composable<SubRoute.Terms> {
		TermsRoute()
	}
}
