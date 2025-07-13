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
		if (intent.action == IntentConstants.ACTION_ALARM_FINISH) {

			alarmFinishedEvent(context, intent)
		}
	}

	private fun alarmFinishedEvent(context: Context, intent: Intent) {
		serviceScope.launch {
			val groupId = intent.getLongExtra(EXTRA_ALARM_ID, 0)
			Timber.i("ID: $groupId 에 대한 사용 시간이 종료되었습니다")
			val appGroup = appGroupRepository.getAppGroupById(groupId)
			val appsPackageNames = appGroup?.apps?.map { it.packageName } ?: emptyList()
			val lastForegroundPackage = lastForegroundPackageName(context)

			if (lastForegroundPackage in appsPackageNames && appGroup != null) {
				Timber.i("ID: $groupId 현재 앱이 사용 중이므로, 차단 오버레이를 표시합니다")

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
