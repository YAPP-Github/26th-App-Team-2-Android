package com.teambrake.brake.presentation.feeback.inquiry.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.feeback.inquiry.InquiryRoute

fun NavController.navigateToInquiry(navOptions: NavOptions? = null) {
	navigate(SubRoute.Feedback.Inquiry, navOptions)
}

fun NavGraphBuilder.inquiryNavGraph() {
	composable<SubRoute.Feedback.Inquiry> {
		InquiryRoute()
	}
}
