package com.teambrake.brake.core.navigation.provider

import com.teambrake.brake.core.navigation.route.Route

interface NavigatorProvider {
	fun getPreviousDestination(): Route?
}
