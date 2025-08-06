package com.yapp.breake.core.detection

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.util.OverlayLauncher
import com.yapp.breake.core.util.getAppNameFromPackage
import com.yapp.breake.domain.usecase.FindAppGroupUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AppLaunchDetectionService : AccessibilityService() {

	@Inject
	lateinit var findAppGroupUsecase: FindAppGroupUseCase

	@Inject
	lateinit var firebaseAnalytics: FirebaseAnalytics

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	override fun onAccessibilityEvent(event: AccessibilityEvent?) {
		if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			val packageName = event.packageName?.toString()
			val className = event.className?.toString()

			if (packageName != null && className != null) {

				if (isActivity(className)) {
					findAppGroupAndAction(packageName)
				}
			}
		}
	}

	private fun isActivity(className: String): Boolean {
		return className.contains(".") && !className.startsWith("android.widget") // 예: android.widget.Toast 제외
	}

	private fun findAppGroupAndAction(packageName: String) {

		serviceScope.launch {
			val appGroup = findAppGroupUsecase(packageName)
			val blockingState = appGroup?.appGroupState

			blockingState?.let {
				Timber.i("앱 그룹: ${appGroup.name}, 상태: $it")
			}

			val appName = getAppNameFromPackage(packageName) ?: packageName

			when (blockingState) {
				AppGroupState.NeedSetting, AppGroupState.Blocking -> {
					OverlayLauncher.startOverlay(
						context = applicationContext,
						appGroup = appGroup,
						appName = appName,
						appGroupState = blockingState,
					)
					firebaseAnalytics.run {
						if (blockingState == AppGroupState.Blocking) {
							logEvent("app_launch_detected") {
								param("app_group_state", "blocking")
								param("app_name", appName)
							}
						} else {
							logEvent("app_launch_detected") {
								param("app_group_state", "need_setting")
								param("app_name", appName)
							}
						}
					}
				}

				AppGroupState.Using -> {
					Timber.i("$packageName 앱은 사용 상태입니다. 아무 작업도 하지 않습니다.")
					firebaseAnalytics.logEvent("app_launch_detected") {
						param("app_group_state", "using")
						param("app_name", appName)
					}
				}

				else -> {}
			}
		}
	}

	override fun onInterrupt() {
		Timber.d("onInterrupt: 접근성 서비스 중단됨")
		// 접근성 서비스 중단에 맞춘 이벤트 정의해야 함
	}

	override fun onServiceConnected() {
		super.onServiceConnected()
	}

	private fun updateServiceInfo(packageNames: Array<String>) {
		val info = AccessibilityServiceInfo().apply {
			eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
			feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
			this.packageNames = packageNames
		}
		this.serviceInfo = info
		Timber.i("접근성 서비스가 업데이트되었고 다음을 모니터링하도록 설정됨: ${packageNames.joinToString()}")
	}

	override fun onDestroy() {
		serviceJob.cancel()
		super.onDestroy()
		Timber.d("접근성 서비스가 소멸되었습니다.")
	}
}
