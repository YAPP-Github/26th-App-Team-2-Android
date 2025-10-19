package com.teambrake.brake.core.navigation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.teambrake.brake.core.navigation.action.MainAction

val LocalMainAction = staticCompositionLocalOf<MainAction> {
	error("No MainAction provided")
}
