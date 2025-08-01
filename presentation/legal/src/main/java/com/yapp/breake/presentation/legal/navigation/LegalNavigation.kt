package com.yapp.breake.presentation.legal.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.SubRoute
import com.yapp.breake.presentation.legal.privacy.PrivacyRoute
import com.yapp.breake.presentation.legal.terms.TermsRoute

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
