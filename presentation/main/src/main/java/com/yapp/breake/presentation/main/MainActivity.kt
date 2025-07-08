package com.yapp.breake.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		WindowCompat.setDecorFitsSystemWindows(window, true)

		setContent {
			val navigator: MainNavigator = rememberMainNavigator()

			BrakeTheme {
				MainScreen(
					navigator = navigator,
					onChangeDarkTheme = { false },
				)
			}
		}
	}
}
