package com.teambrake.brake.data.remote.retrofit

import android.os.Build
import com.teambrake.brake.data.BuildConfig

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
		val BASE_URL
			get() = if (BuildConfig.DEBUG) {
				"https://dev-brake.r-e.kr/"
			} else {
				"https://brake.r-e.kr/"
			}
	}
}
