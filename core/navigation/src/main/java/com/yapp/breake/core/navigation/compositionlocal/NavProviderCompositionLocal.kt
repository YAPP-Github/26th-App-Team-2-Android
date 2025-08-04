package com.yapp.breake.core.navigation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.yapp.breake.core.navigation.provider.NavigatorProvider

val LocalNavigatorProvider = staticCompositionLocalOf<NavigatorProvider> {
	error("No NavProvider provided")
}
