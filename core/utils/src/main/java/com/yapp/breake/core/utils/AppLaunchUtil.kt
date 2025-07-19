package com.yapp.breake.core.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import com.yapp.breake.core.model.app.AppGroup

object AppLaunchUtil {
	fun isAppLaunching(
		context: Context,
		appGroup: AppGroup,
	): Boolean {
		val appsPackageNames = appGroup.apps.map { it.packageName }
		val lastForegroundPackage = lastForegroundPackageName(context)
		return lastForegroundPackage in appsPackageNames
	}

	fun lastForegroundPackageName(context: Context): String? {
		val usageStatsManager =
			context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
		val currentTime = System.currentTimeMillis()
		val events = usageStatsManager.queryEvents(currentTime - 1000 * 10, currentTime) // 최근 10초
		val event = UsageEvents.Event()
		var lastForegroundPackage: String? = null
		while (events.hasNextEvent()) {
			events.getNextEvent(event)
			if (event.eventType == UsageEvents.Event.ACTIVITY_RESUMED ||
				event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND
			) {
				lastForegroundPackage = event.packageName
			}
		}
		return lastForegroundPackage
	}
}
