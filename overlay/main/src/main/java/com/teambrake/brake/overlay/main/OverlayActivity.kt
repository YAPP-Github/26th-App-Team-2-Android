package com.teambrake.brake.overlay.main

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.teambrake.brake.core.common.BlockingConstants
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.core.util.OverlayData
import com.teambrake.brake.overlay.blocking.BlockingOverlay
import com.teambrake.brake.overlay.main.utils.OverlayViewHolder
import com.teambrake.brake.overlay.snooze.SnoozeRoute
import com.teambrake.brake.overlay.timer.TimerRoute
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class OverlayActivity : ComponentActivity() {

	private val overlayViewHolder by lazy { OverlayViewHolder(this) }

	/**
	 * Back 버튼을 눌렀을 때, 해당 액티비티 즉각 종료
	 *
	 * Timer
	 */
	private val callback = object : OnBackPressedCallback(true) {
		override fun handleOnBackPressed() {
			onExitManageApp()
		}
	}

	private val closeReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			if (intent?.action == BlockingConstants.ACTION_CLOSE_OVERLAY) {
				Timber.d("종료 신호를 받았습니다. OverlayActivity를 종료합니다.")
				removeOverlay()
			}
		}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		Timber.d("OverlayActivity onCreate called")

		val filter = IntentFilter(BlockingConstants.ACTION_CLOSE_OVERLAY)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			registerReceiver(closeReceiver, filter, RECEIVER_NOT_EXPORTED)
		} else {
			@SuppressLint("UnspecifiedRegisterReceiverFlag")
			registerReceiver(closeReceiver, filter)
		}

		onBackPressedDispatcher.addCallback(this, callback)
		showOverlay(intent.action)
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

		BrakeTheme {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.background),
			) {
				when (overlayData.appGroupState) {
					AppGroupState.NeedSetting -> {
						TimerRoute(
							appName = overlayData.appName,
							groupName = overlayData.groupName,
							groupId = overlayData.groupId,
							onExitManageApp = ::onExitManageApp,
							onCloseOverlay = ::removeOverlay,
						)
					}

					AppGroupState.SnoozeBlocking -> {
						SnoozeRoute(
							groupId = overlayData.groupId,
							groupName = overlayData.groupName,
							snoozesCount = overlayData.snoozesCount,
							onCloseOverlay = ::removeOverlay,
							onStartHome = ::onStartHome,
							onExitManageApp = ::onExitManageApp,
						)
					}

					AppGroupState.Blocking -> {
						BlockingOverlay(
							appName = overlayData.appName,
							groupName = overlayData.groupName,
							onStartHome = ::onStartHome,
							onExitManageApp = ::onExitManageApp,
						)
					}

					AppGroupState.Using -> {}
				}
			}
		}
	}

	override fun onUserLeaveHint() {
		super.onUserLeaveHint()
		Timber.d("onUserLeaveHint called")

		// Recent Apps 버튼, 홈 버튼, Back 버튼을 눌렀을 때, 즉 오버레이 화면을 벗어나면 해당 액티비티 즉각 종료
		removeOverlay()
	}

	// 액티비티가 종료될 때 오버레이 뷰 제거
	override fun onStop() {
		super.onStop()
		Timber.d("onStop called")
		removeOverlay()
	}

	private fun removeOverlay() {
		overlayViewHolder.remove()
		finishAndRemoveTask()
	}

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		Timber.d("onNewIntent called with action: ${intent.action}")
		setIntent(intent)
		showOverlay(intent.action)
	}

	private fun onStartHome() {
		val homeIntent = packageManager.getLaunchIntentForPackage(packageName)?.apply {
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
		}
		homeIntent?.let { startActivity(it) }
		finish()
	}

	private fun onExitManageApp() {
		val homeIntent = Intent(Intent.ACTION_MAIN).apply {
			addCategory(Intent.CATEGORY_HOME)
			flags = Intent.FLAG_ACTIVITY_NEW_TASK
		}
		removeOverlay()
		startActivity(homeIntent)
	}

	override fun onDestroy() {
		super.onDestroy()
		unregisterReceiver(closeReceiver)
	}
}
