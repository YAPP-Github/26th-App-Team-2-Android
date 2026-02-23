package com.teambrake.brake.core.navigation.route

data class RouteStack(
	val backStack: List<Route> = emptyList(),
) {
	val current: Route? = backStack.lastOrNull()

	val previous: Route? = backStack.dropLast(1).lastOrNull()

	constructor(startDestination: Route) : this(backStack = listOf(startDestination))
}
