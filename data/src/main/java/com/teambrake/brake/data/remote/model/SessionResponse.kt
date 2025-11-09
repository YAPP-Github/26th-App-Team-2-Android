package com.teambrake.brake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class SessionResponse(
	val data: SessionData,
)

@Serializable
internal data class SessionData(
	val sessionId: Long,
)
