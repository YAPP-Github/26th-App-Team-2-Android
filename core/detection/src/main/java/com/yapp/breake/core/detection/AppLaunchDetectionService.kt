package com.yapp.breake.core.detection

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.model.accessibility.IntentConfig
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.util.OverlayLauncher
import com.yapp.breake.core.util.getAppNameFromPackage
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.FindAppGroupUseCase
import com.yapp.breake.domain.usecase.SetAlarmUseCase
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
	lateinit var appGroupRepository: AppGroupRepository

	@Inject
	lateinit var setAlarmUsecase: SetAlarmUseCase

	@Inject
	lateinit var firebaseAnalytics: FirebaseAnalytics

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	/** 현재 유저의 사용 앱 캐싱, AccessibilityService 활용이 가장 정확도가 높음 **/
	private var currentAppPkg: String? = null

	/**
	 * 동적 BroadcastReceiver 정의
	 *
	 * NotificationReceiver에서 전달받은 인텐트를 처리하여 오버레이 띄우기 시도
	 */
	private val commandReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent?) {
			val pkg = intent?.`package`
			if (pkg == applicationContext.packageName) {
				Timber.i(
					"NotificationReceiver -> Accessibility 명령 수신됨: ${intent.action}, 그룹 ID: ${
						intent.getLongExtra(
							IntentConfig.EXTRA_GROUP_ID,
							0,
						)
					}",
				)
				showOverlay(intent)
			}
		}
	}

	/**
	 * registerReceiver() 를 통해 동적 BroadcastReceiver 등록
	 *
	 * IntentFilter 를 통해 송신자가 수신자 (해당 BroadcastReceiver) 를 식별할 수 있도록 설정
	 */
	override fun onCreate() {
		super.onCreate()
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			registerReceiver(
				commandReceiver,
				IntentFilter(IntentConfig.RECEIVER_IDENTITY),
				RECEIVER_NOT_EXPORTED,
			)
		} else {
			@SuppressLint("UnspecifiedRegisterReceiverFlag")
			registerReceiver(
				commandReceiver,
				IntentFilter(IntentConfig.RECEIVER_IDENTITY),
			)
		}
	}

	override fun onAccessibilityEvent(event: AccessibilityEvent?) {
		if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			val packageName = event.packageName?.toString()
			val className = event.className?.toString()

			if (packageName != null && className != null) {

				if (isActivity(className)) {
					if (packageName == applicationContext.packageName) {
						return
					} else {
						Timber.i("앱 실행 감지: 패키지명: $packageName, 클래스명: $className")
						currentAppPkg = packageName
					}

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

	/**
	 * 해당 함수까지의 작업 흐름
	 *
	 * 1. NotificationReceiver에서 예약된 세션 시간 알림을 감지하고, sendBroadcast 로 인텐트(세션 그룹 데이터)를 전달
	 * 2. AccessibilityService에서 인텐트를 받아 가공하고, 현재 사용 어플이 세션의 관리 대상 어플이면 Overlay 띄우기
	 **/
	private fun showOverlay(intent: Intent) {
		val groupId = intent.getLongExtra(IntentConfig.EXTRA_GROUP_ID, 0)
		val appGroupState = getGroupStateFromIntent(intent)

		serviceScope.launch {
			val appGroup = appGroupRepository.getAppGroupById(groupId)
			val appsPkgs = appGroup?.let { it.apps.map { it.packageName }.toSet() } ?: emptySet()

			// Edge case : 재부팅 후 즉각 관리 앱을 실행한 경우 해당 AccessibilityService가 시작되기 전에 앱이 실행될 수 있음
			currentAppPkg ?: monitorCurrentAppLaunch(appsPkgs)

			// 관리 앱이 실행 중인 경우 오버레이 띄우기
			appGroup?.apps?.forEach {
				if (it.packageName == currentAppPkg) {
					val appName = appGroup.apps.find { it.packageName == currentAppPkg }?.name
					OverlayLauncher.startOverlay(
						context = applicationContext,
						appGroup = appGroup,
						appName = appName,
						appGroupState = appGroupState,
						snoozesCount = appGroup.snoozesCount,
					)
					return@launch
				}
			}
			setAlarmUsecase(
				groupId = groupId,
				appGroupState = AppGroupState.Blocking,
				appName = appGroup?.let { it.apps.find { it.packageName == currentAppPkg }?.name }
					?: "Unknown App",
				// 현재 appName은 사실상 사용 안함
			)
		}
	}

	private fun getGroupStateFromIntent(intent: Intent): AppGroupState {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getSerializableExtra(IntentConfig.EXTRA_GROUP_STATE, AppGroupState::class.java)
				?: AppGroupState.Blocking
		} else {
			@Suppress("DEPRECATION")
			intent.getSerializableExtra(IntentConfig.EXTRA_GROUP_STATE) as? AppGroupState
				?: AppGroupState.Blocking
		}
	}

	private fun monitorCurrentAppLaunch(monitoredApps: Set<String>) {
		val root = this.rootInActiveWindow
		if (root != null) {
			val className = root.className?.toString()
			Timber.i("현재 활성화된 윈도우 패키지명: ${root.packageName}, 클래스명: $className")
			if (className != null && monitoredApps.contains(root.packageName)) {
				currentAppPkg = root.packageName?.toString()
				return
			}
		}
		Timber.i("현재 뷰와 대응되는 앱 ${monitoredApps.joinToString(", ")} 을 발견하지 못했습니다.")
	}

	override fun onInterrupt() {
		Timber.d("onInterrupt: 접근성 서비스 중단됨")
		// 접근성 서비스 중단에 맞춘 이벤트 정의해야 함
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
		unregisterReceiver(commandReceiver)
		super.onDestroy()
		Timber.d("접근성 서비스가 소멸되었습니다.")
	}
}
