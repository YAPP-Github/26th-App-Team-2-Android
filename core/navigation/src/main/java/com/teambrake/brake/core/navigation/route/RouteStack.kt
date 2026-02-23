package com.teambrake.brake.core.navigation.route

data class RouteStack(
	val stack: List<Route> = emptyList(),
) {
	val current: Route? = stack.lastOrNull()

	val previous: Route? = stack.dropLast(1).lastOrNull()

	constructor(startDestination: Route): this(stack = listOf(startDestination))
}
