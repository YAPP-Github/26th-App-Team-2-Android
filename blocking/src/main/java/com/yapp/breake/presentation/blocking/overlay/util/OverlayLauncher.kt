package com.yapp.breake.presentation.blocking.overlay.util

import android.content.Context
import android.content.Intent
import com.yapp.breake.core.common.IntentConstants
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.BlockingState
import com.yapp.breake.presentation.blocking.overlay.OverlayData
import timber.log.Timber

object OverlayLauncher {
	fun showTimeSettingOverlay(context: Context, appGroup: AppGroup) {
		startOverlayActivity(context, BlockingState.NEEDS_SETTING, appGroup)
		Timber.i("앱 사용 시간을 설정하기 위한 오버레이를 표시합니다: ${appGroup.name}")
	}

	fun showBlockingOverlay(context: Context, appGroup: AppGroup) {
		startOverlayActivity(context, BlockingState.BLOCKING, appGroup)
		Timber.i("앱이 차단 상태입니다. 차단 오버레이를 표시합니다: ${appGroup.name}")
	}

	private fun startOverlayActivity(
		context: Context,
		blockingState: BlockingState,
		appGroup: AppGroup,
	) {
		Timber.d("startOverlayActivity 호출")

		val overlayData = OverlayData(
			blockingState = blockingState,
			groupId = appGroup.id,
			canCooldown = appGroup.canCooldown,
		)

		val intent = Intent(IntentConstants.ACTION_SHOW_OVERLAY).apply {
			addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			putExtra(IntentConstants.EXTRA_OVERLAY_DATA, overlayData)
		}
		context.startActivity(intent)
	}
}
