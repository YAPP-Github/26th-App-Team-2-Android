package com.yapp.breake.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.yapp.breake.core.designsystem.component.BrakeSnackbarType
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.navigation.action.MainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalNavigatorAction
import com.yapp.breake.core.navigation.route.Route
import com.yapp.breake.presentation.main.navigation.MainNavigator
import com.yapp.breake.presentation.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private lateinit var startDestination: Route
	private val viewModel: MainViewModel by viewModels()

	override fun onStart() {
		super.onStart()
		startDestination = viewModel.decideStartDestination(context = this)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		setContent {
			val navigator: MainNavigator = rememberMainNavigator(startDestination)
			val coroutineScope: CoroutineScope = rememberCoroutineScope()
			val snackBarHostState = remember { SnackbarHostState() }

			val mainAction = object : MainAction {
				override fun onFinish() = finish()
				override fun onShowErrorMessage(message: String) {
					coroutineScope.launch {
						snackBarHostState.showSnackbar(
							message = message,
							actionLabel = BrakeSnackbarType.ERROR.name,
							duration = SnackbarDuration.Short,
							withDismissAction = false,
						)
					}
				}
				override fun onShowSuccessMessage(message: String) {
					coroutineScope.launch {
						snackBarHostState.showSnackbar(
							message = message,
							actionLabel = BrakeSnackbarType.SUCCESS.name,
							duration = SnackbarDuration.Short,
							withDismissAction = false,
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
						snackBarHostState = snackBarHostState,
					)
				}
			}
		}
	}
}
