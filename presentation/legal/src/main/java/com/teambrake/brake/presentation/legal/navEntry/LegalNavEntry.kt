package com.teambrake.brake.presentation.legal.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.legal.privacy.PrivacyRoute
import com.teambrake.brake.presentation.legal.terms.TermsRoute

fun EntryProviderScope<NavKey>.legalNavEntries() {
	entry<SubRoute.Privacy> {
		PrivacyRoute()
	}
	entry<SubRoute.Terms> {
		TermsRoute()
	}
}
