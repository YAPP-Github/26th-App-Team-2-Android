package com.teambrake.brake.core.navigation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.teambrake.brake.core.navigation.action.NavigatorAction

val LocalNavigatorAction = staticCompositionLocalOf<NavigatorAction> {
	error("No NavAction provided")
}
