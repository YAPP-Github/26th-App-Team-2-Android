package com.teambrake.brake.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Paddings = BrakePadding()

val LocalPadding = staticCompositionLocalOf { BrakePadding() }
val LocalDynamicPaddings = staticCompositionLocalOf { DynamicPaddingsProvider() }

@Immutable
data class BrakePadding(
	val screenPaddingHorizontal: Dp = 16.dp,
)

data class DynamicPaddings(
	val bottomNavBarHeight: Dp = 100.dp,
	val oneButtonHeight: Dp = 80.dp,
	val twoButtonHeight: Dp = 130.dp,
)

class DynamicPaddingsProvider {
	var paddings by mutableStateOf(DynamicPaddings())
		private set

	fun updateBottomNavHeight(height: Dp) {
		paddings = paddings.copy(bottomNavBarHeight = height)
	}

	fun updateOneButtonHeight(height: Dp) {
		paddings = paddings.copy(oneButtonHeight = height)
	}

	fun updateTwoButtonHeight(height: Dp) {
		paddings = paddings.copy(twoButtonHeight = height)
	}
}
