package com.yapp.breake.core.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverlayData(
	val appGroupState: String,
	val groupId: Long,
) : Parcelable
