package com.teambrake.brake.core.detection

import android.accessibilityservice.AccessibilityService
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import com.teambrake.brake.core.model.accessibility.IntentConfig
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.core.util.OverlayLauncher
import com.teambrake.brake.core.util.getAppNameFromPackage
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
	lateinit var cachedDatabase: CachedDatabase

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

	// StateFlow의 최신 값을 저장할 변수
	private var cachedGroups: List<CachedTargetAppGroup> = emptyList()

	/** 현재 유저의 사용 앱 캐싱, AccessibilityService 활용이 가장 정확도가 높음 **/
	private var currentAppPkg: String? = null
	private var previousAppPkg: String? = null

	private var isScreenOn: Boolean = true

	/**
	 * 차단 예약 알람 이벤트의 동적 BroadcastReceiver 정의
	 *
	 * NotificationReceiver에서 전달받은 알람의 인텐트를 감지하여 오버레이 띄우기 시도
	 */
	private val alarmReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent) {
			val pkg = intent.`package`
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
	 * 화면 켜짐/꺼짐 이벤트의 동적 BroadcastReceiver 정의
	 *
	 * 화면이 꺼진 상태에서 백그라운드 실행 (특히 유튜브 백그라운드) 으로 인해 AccessibilityEvent가 계속 발생하는 문제 방지
	 */
	private val screenReaderReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent) {
			when (intent.action) {
				Intent.ACTION_SCREEN_ON -> {
					if (!isScreenOn) {
						isScreenOn = true
					}
				}

				Intent.ACTION_SCREEN_OFF -> {
					if (isScreenOn) {
						isScreenOn = false
					}
				}
			}
		}
	}

	/**
	 * registerReceiver() 를 통해 동적 BroadcastReceiver 등록
	 *
	 * IntentFilter 를 통해 송신자가 수신자 (해당 BroadcastReceiver) 를 식별할 수 있도록 설정
	 */
	@SuppressLint("UnspecifiedRegisterReceiverFlag")
	override fun onCreate() {
		super.onCreate()

		val screenIntentFilter = IntentFilter().apply {
			addAction(Intent.ACTION_SCREEN_ON)
			addAction(Intent.ACTION_SCREEN_OFF)
		}

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			registerReceiver(
				alarmReceiver,
				IntentFilter(IntentConfig.RECEIVER_IDENTITY),
				RECEIVER_NOT_EXPORTED,
			)
			registerReceiver(
				screenReaderReceiver,
				screenIntentFilter,
				RECEIVER_NOT_EXPORTED,
			)
		} else {
			registerReceiver(alarmReceiver, IntentFilter(IntentConfig.RECEIVER_IDENTITY))
			registerReceiver(screenReaderReceiver, screenIntentFilter)
		}

		// 서비스 시작 시 캐싱 구독 초기화
		serviceScope.launch {
			cachedDatabase.cachedAppGroups.collect { groups ->
				cachedGroups = groups
			}
		}
	}

	override fun onAccessibilityEvent(event: AccessibilityEvent?) {
		if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
			if (!isScreenOn) return

			val packageName = event.packageName?.toString()
			val className = event.className?.toString()

			if (packageName != null && className != null) {

				if (isActivity(className)) {
					if (packageName == applicationContext.packageName) {
						return
					} else {
						Timber.i("앱 실행 감지: 패키지명: $packageName, 클래스명: $className")
						previousAppPkg = currentAppPkg
						currentAppPkg = packageName
					}

					if (isRecentsScreen(packageName, className)) {
						OverlayLauncher.closeOverlay(applicationContext)
						return
					}

					findAppGroupAndAction(packageName)
				}
			}
		}
	}

	private fun isActivity(className: String): Boolean {
		return className.contains(".") && !className.startsWith("android.widget") // 예: android.widget.Toast 제외
	}

	private fun isRecentsScreen(pkg: String, cls: String): Boolean {
		// pixel 디바이스에서 Recent Screen 이동 시 서비스 앱 Activity 의 onPause 감지 불가
		// 따라서 AccessibilityEvent 로 Recent Screen 진입 감지하여 오버레이 종료 처리
		if (pkg == "com.android.systemui" &&
			(
				cls.contains("Recents", ignoreCase = true) ||
					cls.contains("Overview", ignoreCase = true)
				)
		) {
			return true
		}

		//  Google Pixel Launcher
		if (pkg == "com.google.android.apps.nexuslauncher" &&
			cls.contains("NexusLauncherActivity", ignoreCase = true)
		) {
			return true
		}

		return false
	}

	private fun findAppGroupAndAction(packageName: String) {

		serviceScope.launch {
			val targetApp = cachedGroups.find { app ->
				app.apps.any { it.packageName == packageName }
			}

			if (targetApp == null) {
				Timber.d("$packageName 는 관리 대상 앱이 아닙니다.")
				return@launch
			}

			val blockingState = targetApp.groupState

			Timber.i("앱 그룹 발견: ${targetApp.groupName}, 상태: $blockingState, 앱: $packageName")

			val appName = getAppNameFromPackage(packageName) ?: packageName

			when (blockingState) {
				AppGroupState.NeedSetting, AppGroupState.Blocking -> {
					OverlayLauncher.startOverlay(
						context = applicationContext,
						groupId = targetApp.groupId,
						groupName = targetApp.groupName,
						appName = appName,
						appGroupState = blockingState,
					)
				}

				AppGroupState.Using -> {
					Timber.i("$packageName 앱은 사용 상태입니다. 아무 작업도 하지 않습니다.")
				}

				else -> {
					Timber.d("$packageName 앱은 상태가 $blockingState 입니다.")
				}
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
			val appGroup = cachedGroups.find { it.groupId == groupId }
			val appsPkgs = appGroup?.apps?.map { app -> app.packageName }?.toSet() ?: emptySet()

			// Edge case : 재부팅 후 즉각 관리 앱을 실행한 경우 해당 AccessibilityService가 시작되기 전에 앱이 실행될 수 있음
			currentAppPkg ?: monitorCurrentAppLaunch(appsPkgs)

			// 관리 앱이 실행 중인 경우 오버레이 띄우기
			appGroup?.apps?.forEach {
				if (it.packageName == currentAppPkg) {
					val appName = appGroup.apps.find { app -> app.packageName == currentAppPkg }?.name
					OverlayLauncher.startOverlay(
						context = applicationContext,
						groupId = appGroup.groupId,
						groupName = appGroup.groupName,
						appName = appName,
						appGroupState = appGroupState,
						snoozesCount = appGroup.snoozesCount,
					)
					return@launch
				}
			}
		}
	}

	private fun getGroupStateFromIntent(intent: Intent): AppGroupState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
		intent.getSerializableExtra(IntentConfig.EXTRA_GROUP_STATE, AppGroupState::class.java)
			?: AppGroupState.Blocking
	} else {
		@Suppress("DEPRECATION")
		intent.getSerializableExtra(IntentConfig.EXTRA_GROUP_STATE) as? AppGroupState
			?: AppGroupState.Blocking
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

	override fun onDestroy() {
		serviceJob.cancel()
		unregisterReceiver(alarmReceiver)
		unregisterReceiver(screenReaderReceiver)
		super.onDestroy()
		Timber.d("접근성 서비스가 소멸되었습니다.")
	}
}
