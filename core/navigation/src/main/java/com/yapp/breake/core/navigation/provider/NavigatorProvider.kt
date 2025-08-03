package com.yapp.breake.core.navigation.provider

import androidx.navigation.NavOptions

interface NavigatorProvider {
	fun getNavOptionsClearingBackStack(): NavOptions
	fun getPreviousDestination(): String
}
