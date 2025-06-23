package com.yapp.breake.core.model.auth.kakao

import com.yapp.breake.core.model.auth.AuthUser

data class KakaoUser(
	override val name: String? = null,
	override val email: String? = null,
): AuthUser
