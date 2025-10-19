package com.teambrake.brake.data.mapper

import com.teambrake.brake.core.common.Constants
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.data.remote.model.SessionRequest
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

internal fun AppGroup.toSessionRequest(): SessionRequest? {
	val startTime = this.sessionStartTime
	val endTime = this.endTime?.minusSeconds(Constants.BLOCKING_TIME)
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
