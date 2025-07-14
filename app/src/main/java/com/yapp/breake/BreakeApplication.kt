package com.yapp.breake

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class BreakeApplication : Application() {
	override fun onCreate() {
		super.onCreate()
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
