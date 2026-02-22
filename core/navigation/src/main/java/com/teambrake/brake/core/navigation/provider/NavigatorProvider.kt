package com.teambrake.brake.core.navigation.provider

import androidx.navigation.NavOptions
import com.teambrake.brake.core.navigation.route.Route

interface NavigatorProvider {
	fun getNavOptionsClearingBackStack(): NavOptions
	fun getPreviousDestination(): Route?
}
