package com.teambrake.brake.core.util

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.teambrake.brake.core.common.BlockingConstants
import com.teambrake.brake.core.model.app.AppGroupState
import timber.log.Timber

object OverlayLauncher {

	fun startOverlay(
		context: Context,
		groupId: Long,
		groupName: String,
		appName: String?,
		appGroupState: AppGroupState,
		snoozesCount: Int = 0,
	) {
		Timber.d("startOverlayActivity 호출")

		val overlayData = OverlayData(
			appGroupState = appGroupState,
			groupId = groupId,
			snoozesCount = snoozesCount,
			appName = appName ?: "Unknown App",
			groupName = groupName,
		)

		// 이미 OverlayActivity가 실행 중인지 확인
		val activityManager = context.getSystemService(
			Context.ACTIVITY_SERVICE,
		) as ActivityManager
		val runningTasks = activityManager.appTasks
		val isOverlayRunning = runningTasks.any { taskInfo ->
			taskInfo.taskInfo.topActivity?.className == "com.teambrake.brake.overlay.main.OverlayActivity"
		}

		if (isOverlayRunning) {
			Timber.d("OverlayActivity가 이미 실행 중입니다. 중복 실행을 방지합니다.")
			return
		}

		val bundle = Bundle().apply {
			// ClassLoader 를 설정하여 해당 Parcelable 클래스를 찾을 수 있도록 함
			classLoader = OverlayData::class.java.classLoader
			putParcelable(BlockingConstants.EXTRA_OVERLAY_DATA, overlayData)
		}

		val intent = Intent(BlockingConstants.ACTION_SHOW_OVERLAY).apply {
			`package` = context.packageName
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or
				Intent.FLAG_ACTIVITY_SINGLE_TOP or
				Intent.FLAG_ACTIVITY_CLEAR_TOP
			putExtras(bundle)
		}

		context.startActivity(intent)
	}

	fun closeOverlay(context: Context) {
		Timber.d("OverlayActivity 종료 신호를 전송합니다.")
		val intent = Intent(BlockingConstants.ACTION_CLOSE_OVERLAY).apply {
			`package` = context.packageName
		}
		context.sendBroadcast(intent)
	}
}
