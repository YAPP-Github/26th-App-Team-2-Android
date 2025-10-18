package com.teambrake.brake.core.navigation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.teambrake.brake.core.navigation.provider.NavigatorProvider

val LocalNavigatorProvider = staticCompositionLocalOf<NavigatorProvider> {
	error("No NavProvider provided")
}
