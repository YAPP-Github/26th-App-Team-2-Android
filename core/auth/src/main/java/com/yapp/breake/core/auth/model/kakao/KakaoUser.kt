package com.yapp.breake.core.auth.model.kakao

import com.yapp.breake.core.auth.model.AuthUser

data class KakaoUser(
	override val name: String? = null,
	override val email: String? = null,
) : AuthUser
