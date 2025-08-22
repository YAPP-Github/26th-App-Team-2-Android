package com.yapp.breake.core.datastore.model

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.model.user.UserStatus.INACTIVE
import kotlinx.serialization.Serializable

@Serializable
data class DatastoreUserToken(
	val accessToken: String?,
	val refreshToken: String?,
	val status: UserStatus,
	// provider: Google 인 경우 로그아웃할 시 clearCredentialState() 실행
	val provider: String? = null,
) {
	companion object {
		val Empty = DatastoreUserToken(
			accessToken = null,
			refreshToken = null,
			status = INACTIVE,
			provider = null,
		)
	}
}
