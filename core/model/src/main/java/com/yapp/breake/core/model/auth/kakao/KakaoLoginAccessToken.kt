package com.yapp.breake.core.model.auth.kakao

import com.yapp.breake.core.model.auth.LoginAccessToken

@JvmInline
value class KakaoLoginAccessToken(override val value: String) : LoginAccessToken
