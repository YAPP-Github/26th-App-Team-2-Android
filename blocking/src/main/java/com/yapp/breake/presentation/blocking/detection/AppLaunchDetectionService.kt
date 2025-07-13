package com.yapp.breake.presentation.blocking.detection

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.BlockingState
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.usecase.FindAppGroupUsecase
import com.yapp.breake.presentation.blocking.overlay.util.OverlayLauncher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AppLaunchDetectionService : AccessibilityService() {

	@Inject
	lateinit var findAppGroupUsecase: FindAppGroupUsecase

	@Inject
	lateinit var appRepository: AppRepository

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

//	private var managedPackageNames: List<String> = emptyList()

	private var managedPackageNames: List<String> = listOf(
		"com.google.android.youtube",
	)
	private val youtube = "com.google.android.youtube"

	override fun onAccessibilityEvent(event: AccessibilityEvent?) {
		if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			val packageName = event.packageName?.toString()
			val className = event.className?.toString()

			if (packageName != null && className != null) {

				if (isActivity(className)) {
					Timber.i("감지된 앱: $packageName")
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
			val blockingState = appGroup?.blockingState
			Timber.i("앱 그룹: ${appGroup?.name}, 상태: $blockingState")

			when (blockingState) {
				BlockingState.NEEDS_SETTING -> OverlayLauncher.showTimeSettingOverlay(
					this@AppLaunchDetectionService,
					appGroup,
				)

				BlockingState.BLOCKING -> OverlayLauncher.showBlockingOverlay(
					this@AppLaunchDetectionService,
					appGroup,
				)

				BlockingState.USING, null -> {
					Timber.i("$packageName 앱은 사용 상태입니다. 아무 작업도 하지 않습니다.")
				}
			}
		}
	}

	override fun onInterrupt() {
		Timber.d("onInterrupt: 접근성 서비스 중단됨")
		// 접근성 서비스 중단에 맞춘 이벤트 정의해야 함
	}

	override fun onServiceConnected() {
		super.onServiceConnected()
		serviceScope.launch {
			appRepository.observeApp()
				.distinctUntilChanged()
				.collect { managedApps ->
					val packageNames = managedApps.map(App::packageName)
					managedPackageNames = packageNames
					updateServiceInfo(packageNames.toTypedArray())
				}
		}
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
