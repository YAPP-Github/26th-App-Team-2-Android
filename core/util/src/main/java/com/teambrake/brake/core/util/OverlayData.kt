package com.teambrake.brake.core.util

import android.os.Parcelable
import com.teambrake.brake.core.model.app.AppGroupState
import kotlinx.parcelize.Parcelize

@Parcelize
data class OverlayData(
	val appGroupState: AppGroupState,
	val groupId: Long,
	val appName: String,
	val groupName: String,
	val snoozesCount: Int = 0,
) : Parcelable
