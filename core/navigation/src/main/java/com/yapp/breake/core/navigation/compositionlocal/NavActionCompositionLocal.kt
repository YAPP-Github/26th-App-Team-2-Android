package com.yapp.breake.core.navigation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.yapp.breake.core.navigation.action.NavigatorAction

val LocalNavigatorAction = staticCompositionLocalOf<NavigatorAction> {
	error("No NavAction provided")
}
