package com.yapp.breake.core.util

import android.os.Parcelable
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverlayData(
	val appGroupState: AppGroupState,
	val groupId: Long,
	val snoozesCount: Int = 0,
) : Parcelable
