package com.yapp.breake.presentation.blocking.overlay

import android.Manifest
import android.app.AlarmManager
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.yapp.breake.core.common.BlockingConstants
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.navigation.action.MainAction
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.core.util.OverlayData
import com.yapp.breake.overlay.blocking.BlockingOverlay
import com.yapp.breake.overlay.snooze.SnoozeRoute
import com.yapp.breake.overlay.timer.TimerOverlay
import com.yapp.breake.presentation.blocking.overlay.component.PermissionRequestScreen
import com.yapp.breake.presentation.blocking.overlay.utils.OverlayViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class OverlayActivity : ComponentActivity() {

	private val overlayViewHolder by lazy { OverlayViewHolder(this) }

	@OptIn(ExperimentalPermissionsApi::class)
	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)
		Timber.d("OverlayActivity onCreate called")
		showOverlay(intent.action)

		setContent {
			BrakeTheme {
				val context = LocalContext.current
				val lifecycleOwner = LocalLifecycleOwner.current
				var hasPermission by remember { mutableStateOf(Settings.canDrawOverlays(context)) }
				var overlayShown by remember {
					mutableStateOf(
						overlayViewHolder.view != null && Settings.canDrawOverlays(context),
					)
				}

				val permissionsToRequest = mutableListOf<String>()
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
					permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
				}

				val multiplePermissionsState = rememberMultiplePermissionsState(
					permissions = permissionsToRequest,
				)

				var allPermissionsGranted by remember { mutableStateOf(false) }
				var systemAlertWindowGranted by remember {
					mutableStateOf(
						Settings.canDrawOverlays(
							context,
						),
					)
				}
				var scheduleExactAlarmGranted by remember {
					mutableStateOf(
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
							(
								context.getSystemService(
									Context.ALARM_SERVICE,
								) as AlarmManager
								).canScheduleExactAlarms()
						} else {
							true
						},
					)
				}
				var usageStatsPermissionGranted by remember {
					mutableStateOf(hasUsageStatsPermission(context))
				}
				var batteryOptimizationIgnored by remember {
					mutableStateOf(isIgnoringBatteryOptimizations(context))
				}

				var accessibilityServiceEnabled by remember {
					mutableStateOf(false)
				}

				LaunchedEffect(
					multiplePermissionsState.allPermissionsGranted,
					systemAlertWindowGranted,
					scheduleExactAlarmGranted,
					usageStatsPermissionGranted,
					accessibilityServiceEnabled,
					batteryOptimizationIgnored,
				) {
					allPermissionsGranted = multiplePermissionsState.allPermissionsGranted &&
						systemAlertWindowGranted &&
						scheduleExactAlarmGranted &&
						usageStatsPermissionGranted &&
						accessibilityServiceEnabled &&
						batteryOptimizationIgnored
				}

				if (!allPermissionsGranted) {
					val onPermissionsUpdatedCallback = {
						systemAlertWindowGranted = Settings.canDrawOverlays(context)
						scheduleExactAlarmGranted =
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
								(context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).canScheduleExactAlarms()
							} else {
								true
							}
						usageStatsPermissionGranted = hasUsageStatsPermission(context)
						batteryOptimizationIgnored = isIgnoringBatteryOptimizations(context)
					}

					DisposableEffect(lifecycleOwner) {
						val observer = LifecycleEventObserver { _, event ->
							if (event == Lifecycle.Event.ON_RESUME) {
								onPermissionsUpdatedCallback()
							}
						}
						lifecycleOwner.lifecycle.addObserver(observer)
						onDispose {
							lifecycleOwner.lifecycle.removeObserver(observer)
						}
					}

					PermissionRequestScreen(
						multiplePermissionsState = multiplePermissionsState,
						onRequestSystemAlertWindow = {
							val intent = Intent(
								Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
								"package:${context.packageName}".toUri(),
							)
							startActivity(intent)
						},
						systemAlertWindowGranted = systemAlertWindowGranted,
						onRequestScheduleExactAlarm = {
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
								val intent = Intent(
									Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM,
									"package:${context.packageName}".toUri(),
								)
								startActivity(intent)
							}
						},
						scheduleExactAlarmGranted = scheduleExactAlarmGranted,
						onRequestUsageStatsPermission = {
							val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
							startActivity(intent)
						},
						usageStatsPermissionGranted = usageStatsPermissionGranted,
						accessibilityServiceEnabled = accessibilityServiceEnabled,
						onRequestAccessibilityService = {
							accessibilityServiceEnabled = true
							val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
							startActivity(intent)
						},
						onRequestIgnoreBatteryOptimizations = {
							val intent =
								Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
							intent.data = "package:${context.packageName}".toUri()
							startActivity(intent)
						},
						batteryOptimizationIgnored = batteryOptimizationIgnored,
						onPermissionsUpdated = onPermissionsUpdatedCallback,
					)
				} else {
					if (!hasPermission) {
						overlayShown = false
					}
				}

				DisposableEffect(lifecycleOwner) {
					val observer =
						LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
							if (event == Lifecycle.Event.ON_RESUME) {
								val currentPermission = Settings.canDrawOverlays(context)
								hasPermission = currentPermission
								val currentOverlayExists = overlayViewHolder.view != null

								if (currentPermission) {
									overlayShown = currentOverlayExists
								} else {
									if (currentOverlayExists) {
										overlayViewHolder.remove()
									}
									overlayShown = false
								}
							}
						}
					lifecycleOwner.lifecycle.addObserver(observer)
					onDispose {
						lifecycleOwner.lifecycle.removeObserver(observer)
					}
				}
			}

			MainBackHandler(
				overlayViewHolder = overlayViewHolder,
			)
		}
	}

	private fun hasUsageStatsPermission(context: Context): Boolean {
		val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
		val mode = appOps.checkOpNoThrow(
			AppOpsManager.OPSTR_GET_USAGE_STATS,
			android.os.Process.myUid(),
			context.packageName,
		)
		return mode == AppOpsManager.MODE_ALLOWED
	}

	private fun isIgnoringBatteryOptimizations(context: Context): Boolean {
		val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
		return pm.isIgnoringBatteryOptimizations(context.packageName)
	}

	private fun showOverlay(action: String?) {
		if (action == null) {
			Timber.w("Action is null, cannot show overlay.")
			return
		}

		val overlayData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent?.getParcelableExtra(
				BlockingConstants.EXTRA_OVERLAY_DATA,
				OverlayData::class.java,
			)
		} else {
			@Suppress("DEPRECATION")
			intent?.getParcelableExtra(BlockingConstants.EXTRA_OVERLAY_DATA)
		}

		Timber.d("showOverlay called with overlayData: $overlayData")
		when (action) {
			BlockingConstants.ACTION_SHOW_OVERLAY -> {
				overlayViewHolder.show(this) {
					OverlayScreens(overlayData)
				}
			}

			else -> Timber.w("Unknown action: $action")
		}
	}

	@Composable
	private fun OverlayScreens(overlayData: OverlayData?) {
		if (overlayData == null) {
			Timber.w("OverlayData is null, cannot display overlay.")
			return
		}

		val snackBarHostState = remember { SnackbarHostState() }
		val coroutineScope: CoroutineScope = rememberCoroutineScope()
		val context = LocalContext.current

		val mainAction = object : MainAction {
			override fun onFinish() = finish()
			override fun onShowSnackBar(throwable: Throwable?) {
				coroutineScope.launch {
					snackBarHostState.showSnackbar(throwable?.message ?: "알 수 없는 오류가 발생했습니다.")
				}
			}
			override fun onShowToast(message: String) = Toast.makeText(
				context,
				message,
				Toast.LENGTH_SHORT,
			).show()
		}

		CompositionLocalProvider(
			LocalMainAction provides mainAction,
		) {
			BrakeTheme {
				when (overlayData.appGroupState) {
					AppGroupState.NeedSetting -> {
						TimerOverlay(
							groupId = overlayData.groupId,
							onFinishApp = ::finish,
						)
					}

					AppGroupState.SnoozeBlocking -> {
						SnoozeRoute(
							groupId = overlayData.groupId,
							snoozesCount = overlayData.snoozesCount,
							onFinishApp = ::finish,
							onStartHome = {
							},
						)
					}

					AppGroupState.Blocking -> {
						BlockingOverlay(
							onStartHome = {
							},
							onFinishApp = ::finish,
						)
					}

					AppGroupState.Using -> {}
				}
			}
		}
	}

	override fun onStop() {
		Timber.d("onStop called")
		overlayViewHolder.remove()
		super.onStop()
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		Timber.d("onNewIntent called with action: ${intent.action}")
		setIntent(intent)
		showOverlay(intent.action)
	}
}

@Composable
private fun MainBackHandler(
	overlayViewHolder: OverlayViewHolder,
) {
	BackHandler {
		overlayViewHolder.remove()
	}
}
