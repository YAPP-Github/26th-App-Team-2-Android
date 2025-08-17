package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class SessionRequest(
    val groupId: Long,
    val start: String,
    val end: String,
    val goalMinutes: Int,
    val snoozeUnit: Int,
    val snoozeCount: Int,
)
