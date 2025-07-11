package com.yapp.breake.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.navigation.action.MainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.net.UnknownHostException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		enableEdgeToEdge()

		setContent {
			val navigator: MainNavigator = rememberMainNavigator()
			val coroutineScope: CoroutineScope = rememberCoroutineScope()
			val snackBarHostState = remember { SnackbarHostState() }

			val mainAction = object : MainAction {
				override fun onFinish() = finish()
				override fun onShowSnackBar(throwable: Throwable?) {
					coroutineScope.launch {
						snackBarHostState.showSnackbar(
							when (throwable) {
								is UnknownHostException -> getString(R.string.network_error)
								else -> getString(R.string.unknown_error)
							},
						)
					}
				}
			}

			CompositionLocalProvider(
				LocalMainAction provides mainAction,
				LocalNavigatorAction provides navigator.navigatorAction(),
			) {
				BrakeTheme {
					MainScreen(
						navigator = navigator,
						onChangeDarkTheme = { false },
					)
				}
			}
		}
	}
}
