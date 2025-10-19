package com.teambrake.brake.core.navigation.provider

import androidx.navigation.NavOptions

interface NavigatorProvider {
	fun getNavOptionsClearingBackStack(): NavOptions
	fun getPreviousDestination(): String
}
