package com.yapp.breake.data.mapper

import com.yapp.breake.core.model.app.Session
import com.yapp.breake.data.remote.model.SessionRequest
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

internal fun Session.toSessionRequest(): SessionRequest {
    return SessionRequest(
        groupId = groupId,
        start = startTime.format(dateFormatter),
        end = endTime.format(dateFormatter),
        goalMinutes = goalMinutes,
        snoozeUnit = snoozeUnit,
        snoozeCount = snoozeCount,
    )
}
