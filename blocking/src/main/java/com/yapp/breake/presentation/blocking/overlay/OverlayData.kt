package com.yapp.breake.presentation.blocking.overlay

import android.os.Parcelable
import com.yapp.breake.core.model.app.BlockingState
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverlayData(
	val blockingState: BlockingState,
	val canCooldown: Boolean,
	val groupId: Long,
) : Parcelable
