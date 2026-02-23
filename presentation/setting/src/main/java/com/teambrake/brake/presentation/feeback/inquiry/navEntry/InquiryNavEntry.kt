package com.teambrake.brake.presentation.feeback.inquiry.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.feeback.inquiry.InquiryRoute

fun EntryProviderScope<NavKey>.inquiryNavEntry() {
	entry<SubRoute.Feedback.Inquiry> {
		InquiryRoute()
	}
}
