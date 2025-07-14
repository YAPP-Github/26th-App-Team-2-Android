package com.yapp.breake.presentation.blocking.util

import android.os.Parcelable
import com.yapp.breake.core.model.app.BlockingState
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverlayData(
	val blockingState: BlockingState,
	val snoozesCount: Int,
	val groupId: Long,
) : Parcelable {

	val snoozeEnabled: Boolean
		get() = snoozesCount < 2
}
