package com.yapp.breake.core.utils

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverlayData(
	val appGroupState: String,
	val groupId: Long,
) : Parcelable
