package com.teambrake.brake.presentation.nickname.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.nickname.NicknameRoute

fun NavController.navigateToNickname(navOptions: NavOptions? = null) {
	navigate(SubRoute.Nickname, navOptions)
}

fun NavGraphBuilder.nicknameNavGraph() {
	composable<SubRoute.Nickname> {
		NicknameRoute()
	}
}
