package com.yapp.breake.presentation.feeback.opinion.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.SubRoute
import com.yapp.breake.presentation.feeback.opinion.OpinionRoute

fun NavController.navigateToOpinion(navOptions: NavOptions? = null) {
	navigate(SubRoute.Feedback.Opinion, navOptions)
}

fun NavGraphBuilder.opinionNavGraph() {
	composable<SubRoute.Feedback.Opinion> {
		OpinionRoute()
	}
}
