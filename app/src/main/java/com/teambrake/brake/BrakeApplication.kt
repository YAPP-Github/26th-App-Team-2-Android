package com.teambrake.brake

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.teambrake.brake.core.auth.google.GoogleAuthManager
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class BrakeApplication :
	Application(),
	Configuration.Provider {

	@Inject lateinit var googleAuthManager: GoogleAuthManager

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
		super.onCreate()

		// GoogleAuthManager 초기화
		googleAuthManager.initializeAuthorizationRequest(
			context = this,
			// 컴파일 타임 때 google-services.json 에서 web client id 의 string resource 생성
			serverClientId = getString(R.string.default_web_client_id),
		)

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
