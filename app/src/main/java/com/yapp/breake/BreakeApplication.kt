package com.yapp.breake

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BreakeApplication : Application() {
	override fun onCreate() {
		super.onCreate()
		initTimber()
		KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
	}

	private fun initTimber() {
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		}
	}
}
