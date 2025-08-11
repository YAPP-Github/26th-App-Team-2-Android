package com.yapp.breake

import android.app.Application
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BreakeApplication : Application(), Configuration.Provider {

	@Inject lateinit var workerFactory: HiltWorkerFactory

	/**
	 * WorkManager 설정을 제공하는 프로퍼티
	 *
	 * HiltWorker 2.1.0 이상 버전의 WorkManager 초기화 공식 방식
	 */
	override val workManagerConfiguration: Configuration
		get() = Configuration.Builder()
			.setWorkerFactory(workerFactory)
			.build()

	override fun onCreate() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			// API 31+ 기본 스플래시 사용
			super.onCreate()
		} else {
			// API 28~30에서는 커스텀 배경 적용
			setTheme(R.style.Theme_Breake_Splash)
			super.onCreate()
		}

		// WorkManager 초기화
		WorkManager.initialize(this, workManagerConfiguration)

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
