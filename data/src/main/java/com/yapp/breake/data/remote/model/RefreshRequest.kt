package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(val refreshToken: String)
