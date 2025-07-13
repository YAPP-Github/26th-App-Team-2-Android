package com.yapp.breake.presentation.blocking.notification

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yapp.breake.presentation.blocking.scheduler.AlarmScheduler.EXTRA_ALARM_ID
import com.yapp.breake.core.common.IntentConstants
import com.yapp.breake.presentation.blocking.overlay.util.OverlayLauncher
import com.yapp.breake.domain.repository.AppGroupRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

	@Inject
	lateinit var appGroupRepository: AppGroupRepository

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	override fun onReceive(context: Context, intent: Intent) {
		Timber.d("onReceive 호출됨, action: ${intent.action}")
		if (intent.action == IntentConstants.ACTION_ALARM_FINISH) {
			alarmFinishedEvent(context, intent)
		}
	}

	private fun alarmFinishedEvent(context: Context, intent: Intent) {
		Timber.d("${IntentConstants.ACTION_ALARM_FINISH} 수신됨.")
		serviceScope.launch {
			val groupId = intent.getLongExtra(EXTRA_ALARM_ID, 0)
			val appGroup = appGroupRepository.getAppGroupById(groupId)
			val appsPackageNames = appGroup?.apps?.map { it.packageName } ?: emptyList()
			val lastForegroundPackage = lastForegroundPackageName(context)

			// 해당 앱 그룹에 현재 사용중인 패키지 앱이 포함되어 있다면, Blocking 화면을 띄움
			if (lastForegroundPackage in appsPackageNames && appGroup != null) {
				OverlayLauncher.showBlockingOverlay(
					context = context,
					appGroup = appGroup,
				)
			}
		}
	}

	private fun lastForegroundPackageName(context: Context): String? {
		val usageStatsManager =
			context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
		val currentTime = System.currentTimeMillis()
		val events = usageStatsManager.queryEvents(currentTime - 1000 * 1, currentTime) // 최근 1초
		val event = UsageEvents.Event()
		var lastForegroundPackage: String? = null
		while (events.hasNextEvent()) {
			events.getNextEvent(event)
			if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
				lastForegroundPackage = event.packageName
			}
		}
		return lastForegroundPackage
	}
}
