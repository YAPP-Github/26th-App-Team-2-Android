package com.yapp.breake.data.api

import android.os.Build

sealed interface ApiConfig {

	data object AndroidID : ApiConfig {
		val deviceInfo = buildString {
			append(Build.MANUFACTURER).append("/")
			append(Build.MODEL).append("/")
			append(Build.DEVICE).append("/")
			append(Build.PRODUCT).append("/")
			append(Build.BRAND).append("/")
			append(Build.BOARD).append("/")
			append(Build.HARDWARE)
		}
		// 서드 파티 사용 없이 최대한 디바이스의 정보를 조합하여 최대한 유니크하게 설정
	}

	data object ServerDomain : ApiConfig {
		const val BASE_URL = "https://brake.r-e.kr/"
	}
}
