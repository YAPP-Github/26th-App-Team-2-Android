package com.yapp.breake.core.utils

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Build
import android.util.LongSparseArray
import com.yapp.breake.core.model.app.AppGroup
import timber.log.Timber

object AppLaunchUtil {

	fun isAppLaunching(
		context: Context,
		appGroup: AppGroup,
	): Boolean {
		val appsPackageNames = appGroup.apps.map { it.packageName }
		val currentForegroundPackage = getCurrentForegroundPackageName(context)
		Timber.d("Current foreground package: $currentForegroundPackage")
		return currentForegroundPackage in appsPackageNames
	}

	private fun getCurrentForegroundPackageName(context: Context): String? {
		val usageStatsManager =
			context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

		var lastRunAppTimeStamp = 0L
		val interval = 1000 * 60 * 5
		val end = System.currentTimeMillis()
		val begin = end - interval

		val packageNameMap = LongSparseArray<String>()
		val usageEvents = usageStatsManager.queryEvents(begin, end)

		while (usageEvents.hasNextEvent()) {
			val event = UsageEvents.Event()
			usageEvents.getNextEvent(event)

			if (isForegroundEvent(event)) {
				packageNameMap.put(event.timeStamp, event.packageName)
				if (event.timeStamp > lastRunAppTimeStamp) {
					lastRunAppTimeStamp = event.timeStamp
				}
			}
		}

		val topPackageName = packageNameMap.get(lastRunAppTimeStamp, "")
		return if (topPackageName.isNotEmpty() && !isSystemApp(topPackageName)) {
			topPackageName
		} else {
			null
		}
	}

	private fun isForegroundEvent(event: UsageEvents.Event?): Boolean {
		if (event == null) return false

		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			event.eventType == UsageEvents.Event.ACTIVITY_RESUMED
		} else {
			event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND
		}
	}

	private fun isSystemApp(packageName: String): Boolean {
		return packageName.startsWith("com.android") ||
			packageName.startsWith("android") ||
			packageName == "com.sec.android.app.launcher" ||
			packageName == "com.google.android.apps.nexuslauncher" ||
			packageName == "com.miui.home" ||
			packageName.contains("launcher")
	}
}
