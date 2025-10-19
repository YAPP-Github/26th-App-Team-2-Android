package com.teambrake.brake.presentation.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.teambrake.brake.presentation.main.component.BrakeSnackbarType
import com.teambrake.brake.core.designsystem.component.DotProgressIndicator
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray900
import com.teambrake.brake.core.navigation.action.MainAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalMainAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalNavigatorAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.teambrake.brake.presentation.main.component.BrakeSnackbarHostState
import com.teambrake.brake.presentation.main.component.LogoutWarningDialog
import com.teambrake.brake.presentation.main.navigation.MainNavigator
import com.teambrake.brake.presentation.main.navigation.rememberMainNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		// 스플래시 스크린 설치, 내부에서 API 31 미만 버전도 호환되도록 처리
		val splashScreen = installSplashScreen()

		enableEdgeToEdge()

		// 스플래시 스크린이 유지되는 조건 설정
		splashScreen.setKeepOnScreenCondition {
			viewModel.startRoute.value == null
		}

		viewModel.decideStartDestination(context = this@MainActivity)

		setContent {
			val startDestination by viewModel.startRoute.collectAsState()

			when (val destination = startDestination) {
				null -> { /* 스플래시 화면 유지 */ }
				else -> {
					val navigator: MainNavigator = rememberMainNavigator(destination)
					val coroutineScope: CoroutineScope = rememberCoroutineScope()
					val snackBarHostState = remember { BrakeSnackbarHostState() }

					val mainAction = object : MainAction {
						@Composable
						override fun OnFinishBackHandler() {
							var backPressedTime by remember { mutableLongStateOf(0L) }
							BackHandler {
								if (System.currentTimeMillis() - backPressedTime <= 2000L) {
									finish()
								} else {
									Toast.makeText(
										this@MainActivity,
										this@MainActivity.getString(
											R.string.exit_message,
										),
										Toast.LENGTH_SHORT,
									).show()
								}
								backPressedTime = System.currentTimeMillis()
							}
						}

						@Composable
						override fun OnShowLogoutDialog(
							onConfirm: () -> Unit,
							onDismiss: () -> Unit,
						) {
							LogoutWarningDialog(
								onConfirm = onConfirm,
								onDismissRequest = onDismiss,
							)
						}

						@Composable
						override fun OnShowLoading() {
							Popup {
								Box(
									modifier = Modifier
										.fillMaxSize()
										.background(Gray900.copy(alpha = 0.8f))
										.statusBarsPadding(),
									contentAlignment = Alignment.Center,
								) {
									DotProgressIndicator()
								}
							}
						}

						override fun onShowErrorMessage(message: String) {
							coroutineScope.launch {
								snackBarHostState.showSnackbar(
									message = message,
									actionLabel = BrakeSnackbarType.ERROR.name,
									duration = 5000L,
									onAction = snackBarHostState::dismiss,
								)
							}
						}

						override fun onShowSuccessMessage(message: String) {
							coroutineScope.launch {
								snackBarHostState.showSnackbar(
									message = message,
									actionLabel = BrakeSnackbarType.SUCCESS.name,
									duration = 3000L,
									onAction = snackBarHostState::dismiss,
								)
							}
						}
					}

					CompositionLocalProvider(
						LocalMainAction provides mainAction,
						LocalNavigatorAction provides navigator.navigatorAction(),
						LocalNavigatorProvider provides navigator.navigatorProvider(),
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
	}

	override fun onDestroy() {
		viewModel.analyzeFinishApp()
		super.onDestroy()
	}
}
