package com.yapp.breake.overlay.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.yapp.breake.core.common.BlockingConstants
import com.yapp.breake.core.designsystem.component.BaseScaffold
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.util.OverlayData
import com.yapp.breake.overlay.blocking.BlockingOverlay
import com.yapp.breake.overlay.main.utils.OverlayViewHolder
import com.yapp.breake.overlay.snooze.SnoozeRoute
import com.yapp.breake.overlay.timer.TimerRoute
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

	override fun onCreate(savedInstanceState: Bundle?) {
		enableEdgeToEdge()
		super.onCreate(savedInstanceState)
		Timber.d("OverlayActivity onCreate called")

		onBackPressedDispatcher.addCallback(this, callback)
		showOverlay(intent.action)

		setContent {
			BrakeTheme {
				BaseScaffold { }
			}
		}
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
			when (overlayData.appGroupState) {
				AppGroupState.NeedSetting -> {
					TimerRoute(
						appName = overlayData.appName,
						groupName = overlayData.groupName,
						groupId = overlayData.groupId,
						onExitManageApp = ::onExitManageApp,
						onCloseOverlay = ::finishAndRemoveTask,
					)
				}

				AppGroupState.SnoozeBlocking -> {
					SnoozeRoute(
						groupId = overlayData.groupId,
						groupName = overlayData.groupName,
						snoozesCount = overlayData.snoozesCount,
						onCloseOverlay = ::finishAndRemoveTask,
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

	// Recent Apps 버튼, Device Home 버튼, Back 버튼을 눌렀을 때, 즉 오버레이 화면을 벗어나면 해당 액티비티 즉각 종료
	override fun onPause() {
		super.onPause()
		overlayViewHolder.remove()
		finishAndRemoveTask()
	}

	// onPause 에서 이미 종료 처리
	override fun onStop() {
		Timber.d("onStop called")
		overlayViewHolder.remove()
		finishAndRemoveTask()
		super.onStop()
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
		finishAndRemoveTask()
		startActivity(homeIntent)
	}
}
