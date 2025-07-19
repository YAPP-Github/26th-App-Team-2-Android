package com.yapp.breake.core.utils

import android.content.Context
import android.content.Intent
import com.yapp.breake.core.common.BlockingConstants
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.model.app.AppGroupState.Companion.name
import timber.log.Timber

object OverlayLauncher {

	fun startOverlay(
		context: Context,
		appGroup: AppGroup,
		appGroupState: AppGroupState,
	) {
		Timber.d("startOverlayActivity 호출")

		val overlayData = OverlayData(
			appGroupState = appGroupState.name,
			groupId = appGroup.id,
		)

		val intent = Intent(BlockingConstants.ACTION_SHOW_OVERLAY).apply {
			`package` = context.packageName
			flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
			putExtra(BlockingConstants.EXTRA_OVERLAY_DATA, overlayData)
		}

		context.startActivity(intent)
	}
}
