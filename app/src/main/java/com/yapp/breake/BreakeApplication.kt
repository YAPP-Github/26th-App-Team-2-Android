package com.yapp.breake

import android.app.Application
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BreakeApplication : Application() {
	override fun onCreate() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			// API 31+ 기본 스플래시 사용
			super.onCreate()
		} else {
			// API 28~30에서는 커스텀 배경 적용
			setTheme(R.style.Theme_Breake_Splash)
			super.onCreate()
		}

		initTimber()
	}

	private fun initTimber() {
		if (BuildConfig.DEBUG) {
			Timber.plant(
				object : Timber.DebugTree() {
					override fun createStackElementTag(element: StackTraceElement): String {
						val fullClassName = element.className
						val className = fullClassName.substringAfterLast('.')
						return "BRAKE/$className"
					}
				},
			)
		}
	}
}
