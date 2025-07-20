package com.yapp.breake.core.util

import android.content.Context
import android.content.Intent
import com.yapp.breake.core.common.BlockingConstants
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import timber.log.Timber

object OverlayLauncher {

	fun startOverlay(
		context: Context,
		appGroup: AppGroup,
		appGroupState: AppGroupState,
		snoozesCount: Int = 0,
	) {
		Timber.d("startOverlayActivity 호출")

		val overlayData = OverlayData(
			appGroupState = appGroupState,
			groupId = appGroup.id,
			snoozesCount = snoozesCount,
		)

		val intent = Intent(BlockingConstants.ACTION_SHOW_OVERLAY).apply {
			`package` = context.packageName
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			putExtra(BlockingConstants.EXTRA_OVERLAY_DATA, overlayData)
		}

		context.startActivity(intent)
	}
}
