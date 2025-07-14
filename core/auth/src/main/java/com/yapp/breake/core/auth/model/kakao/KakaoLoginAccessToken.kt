package com.yapp.breake.core.auth.model.kakao

import com.yapp.breake.core.auth.model.LoginAccessToken

@JvmInline
value class KakaoLoginAccessToken(override val value: String) : LoginAccessToken
