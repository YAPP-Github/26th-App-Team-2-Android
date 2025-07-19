package com.yapp.breake.core.utils

import android.os.Parcelable
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class OverlayData(
	val appGroupState: @RawValue AppGroupState,
	val groupId: Long,
) : Parcelable
