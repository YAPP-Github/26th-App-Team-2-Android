package com.yapp.breake.core.model.user

sealed interface Destination {
	data object Login : Destination
	data object Onboarding : Destination
	data object PermissionOrHome : Destination
}
