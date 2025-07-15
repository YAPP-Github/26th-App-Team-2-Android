package com.yapp.breake.core.navigation.compositionlocal

import androidx.compose.runtime.staticCompositionLocalOf
import com.yapp.breake.core.navigation.action.MainAction

val LocalMainAction = staticCompositionLocalOf<MainAction> {
	error("No MainAction provided")
}
