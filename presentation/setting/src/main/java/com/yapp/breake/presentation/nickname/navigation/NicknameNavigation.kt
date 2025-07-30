package com.yapp.breake.presentation.nickname.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.SubRoute
import com.yapp.breake.presentation.nickname.NicknameRoute

fun NavController.navigateToNickname(navOptions: NavOptions? = null) {
	navigate(SubRoute.Nickname, navOptions)
}

fun NavGraphBuilder.nicknameNavGraph() {
	composable<SubRoute.Nickname> {
		NicknameRoute()
	}
}
