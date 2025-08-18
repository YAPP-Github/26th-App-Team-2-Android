package com.yapp.breake.data.mapper

import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.data.remote.model.SessionRequest
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

internal fun AppGroup.toSessionRequest(): SessionRequest? {
	val startTime = this.sessionStartTime
	val endTime = this.endTime
	val goalMinutes = this.goalMinutes

	if (startTime == null || endTime == null || goalMinutes == null) {
		return null
	}

	return SessionRequest(
		groupId = id,
		start = startTime.format(dateFormatter),
		end = endTime.format(dateFormatter),
		goalMinutes = goalMinutes,
		snoozeUnit = Constants.SNOOZE_MINUTES.toInt(),
		snoozeCount = snoozesCount,
	)
}
