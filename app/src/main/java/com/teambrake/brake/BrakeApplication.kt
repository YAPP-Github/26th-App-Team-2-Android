package com.teambrake.brake

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import com.amplitude.android.Amplitude
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

	@Inject lateinit var amplitudeInstance: Amplitude

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

		registerAmplitudeLifecycleCallbacks()
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

	// 앱이 백그라운드로 갈 때 Amplitude 이벤트큐에 쌓인 이벤트를 플러시(전송)하기 위한 콜백 등록
	// 백그라운드 예시) 앱 서비스 내부에서 권한 Activity 실행 시, 홈 버튼 클릭 등
	private fun registerAmplitudeLifecycleCallbacks() {
		registerActivityLifecycleCallbacks(
			object : ActivityLifecycleCallbacks {
				override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
				override fun onActivityCreated(p0: Activity, p1: Bundle?) {}
				override fun onActivityStarted(activity: Activity) {}
				override fun onActivityResumed(activity: Activity) {}
				override fun onActivityPaused(activity: Activity) {}

				override fun onActivityStopped(activity: Activity) {
					amplitudeInstance.flush()
				}

				override fun onActivityDestroyed(activity: Activity) {
					amplitudeInstance.flush()
				}
			},
		)
	}
}
