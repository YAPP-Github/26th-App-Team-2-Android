package com.yapp.breake.presentation.feeback.inquiry.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.SubRoute
import com.yapp.breake.presentation.feeback.inquiry.InquiryRoute

fun NavController.navigateToInquiry(navOptions: NavOptions? = null) {
	navigate(SubRoute.Feedback.Inquiry, navOptions)
}

fun NavGraphBuilder.inquiryNavGraph() {
	composable<SubRoute.Feedback.Inquiry> {
		InquiryRoute()
	}
}
