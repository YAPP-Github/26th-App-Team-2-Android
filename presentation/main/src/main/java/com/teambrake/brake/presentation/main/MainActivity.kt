package com.teambrake.brake.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Popup
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amplitude.android.Amplitude
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.amplitude.TriggerSource
import com.teambrake.brake.core.designsystem.component.DotProgressIndicator
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.model.notification.NotificationIntentConfig
import com.teambrake.brake.core.navigation.action.MainAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalMainAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalNavigatorAction
import com.teambrake.brake.core.navigation.compositionlocal.LocalNavigatorProvider
import com.teambrake.brake.presentation.main.component.BrakeSnackbarHostState
import com.teambrake.brake.presentation.main.component.BrakeSnackbarType
import com.teambrake.brake.presentation.main.component.LogoutWarningDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	@Inject
	lateinit var amplitude: Amplitude

	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		// 앱 시작 이벤트 트래킹
		trackAmplitudeOpenAppEvent(intent)

		// 세로 방향으로 고정
		@SuppressLint("SourceLockedOrientationActivity")
		requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

		// 스플래시 스크린 설치, 내부에서 API 31 미만 버전도 호환되도록 처리
		val splashScreen = installSplashScreen()

		setContent {
			val routeStack by viewModel.routeStack.collectAsStateWithLifecycle()

			// 스플래시 스크린이 유지되는 조건 설정
			splashScreen.setKeepOnScreenCondition {
				routeStack.backStack.isEmpty()
			}

			viewModel.decideStartDestination(context = this@MainActivity)

			if (routeStack.backStack.isEmpty()) return@setContent

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
								.background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
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
				LocalNavigatorAction provides viewModel.navigatorAction(),
				LocalNavigatorProvider provides viewModel.navigatorProvider(),
			) {
				BrakeTheme {
					MainScreen(
						routeStack = routeStack,
						onTabSelected = viewModel::navigateTab,
						onChangeDarkTheme = { false },
						snackBarHostState = snackBarHostState,
					)
				}
			}
		}
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		setIntent(intent)

		// 앱이 백그라운드에서의 재진입 (notification 을 통해 진입하는 방식 포함) 이벤트 트래킹
		trackAmplitudeOpenAppEvent(intent)
	}

	override fun onDestroy() {
		viewModel.analyzeFinishApp()
		super.onDestroy()
	}

	/**
	 * open_app 이벤트 트래킹
	 * TriggerSource에 따라 앱 진입 경로를 추적
	 */
	private fun trackAmplitudeOpenAppEvent(intent: Intent) {
		val isFromNotification =
			intent.getBooleanExtra(NotificationIntentConfig.EXTRA_OPEN_APP, false)
		val triggerSource = if (isFromNotification) {
			TriggerSource.NOTIFICATION
		} else {
			TriggerSource.APP_ICON
		}

		val event = AmplitudeEventHelper.createOpenAppEvent(triggerSource)
		amplitude.track(
			eventType = event.getEventName(),
			eventProperties = event.toEventProperties(),
		)
	}
}
