package com.yapp.breake.core.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
	primary = ButtonYellow,
	onPrimary = Gray900,
	secondary = Gray100,
	onSecondary = Gray800,
	error = Error,
	surface = Gray850,
	surfaceVariant = Gray800,
	onSurface = White,
	onSurfaceVariant = Gray300,
	outline = Gray800,
	outlineVariant = Gray800,
	background = Gray900,
	onBackground = White,
)

@Composable
fun BrakeTheme(
	darkTheme: Boolean = true,
	content: @Composable () -> Unit,
) {
	val colorScheme = DarkColorScheme

	if (!LocalInspectionMode.current) {
		val view = LocalView.current
		SideEffect {
			val window = (view.context as Activity).window
			WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
			WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
		}
	}

	CompositionLocalProvider(
		LocalTypography provides Typography,
		LocalPadding provides Paddings,
	) {
		MaterialTheme(
			colorScheme = colorScheme,
			content = content,
		)
	}
}

object BrakeTheme {

	val typography: BrakeTypography
		@Composable
		get() = LocalTypography.current

	val paddings: BrakePadding
		@Composable
		get() = LocalPadding.current
}
