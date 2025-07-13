package com.yapp.breake.core.detection

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.yapp.breake.core.common.IntentConstants
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.BlockingState
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.usecase.FindAppGroupUsecase
import com.yapp.breake.presentation.overlay.OverlayData
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
				Timber.i("감지된 앱: $packageName, 클래스: $className, 실제 Activity 창입니다.")
				val b = packageName == youtube && isActivity(className)
				Timber.d("isActivity: $b")
				if (b) {
//					findAppGroupAndAction(packageName)
					showTimeSettingOverlay(
						packageName,
						AppGroup(
							id = 1L,
							name = "YouTube",
							blockingState = BlockingState.NEEDS_SETTING,
							apps = listOf(),
							snoozes = listOf(),
						),
					)
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

			when (appGroup?.blockingState) {
				BlockingState.NEEDS_SETTING -> showTimeSettingOverlay(packageName, appGroup)
				BlockingState.BLOCKING -> showBlockingOverlay(packageName, appGroup)
				BlockingState.USING, null -> {
					Timber.i("$packageName 앱은 사용 상태입니다. 아무 작업도 하지 않습니다.")
				}
			}
		}
	}

	private fun showTimeSettingOverlay(packageName: String, appGroup: AppGroup) {
		startOverlayActivity(packageName, BlockingState.NEEDS_SETTING, appGroup)
		Timber.i("앱 사용 시간을 설정하기 위한 오버레이를 표시합니다: $packageName")
	}

	private fun showBlockingOverlay(packageName: String, appGroup: AppGroup) {
		startOverlayActivity(packageName, BlockingState.BLOCKING, appGroup)
		Timber.i("앱이 차단 상태입니다. 차단 오버레이를 표시합니다: $packageName")
	}

	private fun startOverlayActivity(
		packageName: String,
		blockingState: BlockingState,
		appGroup: AppGroup,
	) {
		Timber.d("startOverlayActivity 호출")

		val overlayData = OverlayData(
			packageName = packageName,
			blockingState = blockingState,
			groupId = appGroup.id,
			canCooldown = appGroup.canCooldown,
		)

		val intent = Intent(IntentConstants.ACTION_SHOW_OVERLAY).apply {
			addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
			addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
			putExtra(IntentConstants.EXTRA_OVERLAY_DATA, overlayData)
		}
		startActivity(intent)
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
