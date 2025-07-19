package com.yapp.breake.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
			val context = this

			val mainAction = object : MainAction {
				override fun onFinish() = finish()
				override fun onShowSnackBar(throwable: Throwable?) {
					coroutineScope.launch {
						snackBarHostState.showSnackbar(throwable?.message ?: "알 수 없는 오류가 발생했습니다.")
					}
				}
				override fun onShowToast(message: String) = Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
