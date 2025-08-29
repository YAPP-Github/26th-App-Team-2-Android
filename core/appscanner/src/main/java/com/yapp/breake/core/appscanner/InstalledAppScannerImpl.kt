package com.yapp.breake.core.appscanner

import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import javax.inject.Inject
import androidx.core.graphics.drawable.toDrawable

class InstalledAppScannerImpl @Inject constructor(
	private val context: Context,
) : InstalledAppScanner {
	override fun getInstalledAppsMetaData(): List<AppMetaData> {
		val pm = context.packageManager
		val usm = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
		// 일주일 기간의 앱 전체 각각의 사용량
		val usageList = usm.queryUsageStats(
			UsageStatsManager.INTERVAL_WEEKLY,
			System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 7,
			System.currentTimeMillis(),
		)
		val mainIntent = Intent(Intent.ACTION_MAIN, null)
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

		val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			pm.queryIntentActivities(
				mainIntent,
				PackageManager.ResolveInfoFlags.of(0L),
			)
		} else {
			pm.queryIntentActivities(mainIntent, 0)
		}

		return resolvedInfos
			.mapNotNull { resolveInfo ->
				val resources =
					pm.getResourcesForApplication(resolveInfo.activityInfo.applicationInfo)
				AppMetaData(
					appName = if (resolveInfo.activityInfo.labelRes != 0) {
						resources.getString(resolveInfo.activityInfo.labelRes)
					} else {
						resolveInfo.activityInfo.applicationInfo.loadLabel(pm).toString()
					},
					packageName = resolveInfo.activityInfo.packageName,
					icon = resolveInfo.activityInfo.loadIcon(pm),
				)
			}
			.sortedByDescending { metaData ->
				usageList.find { it.packageName == metaData.packageName }?.let {
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
						it.totalTimeInForeground
					} else {
						it.lastTimeUsed
					}
				}
			}
			.filterNot { metaData ->
				metaData.packageName == context.packageName
			}
	}

	override fun getIconDrawable(packageName: String): Drawable {
		val pm = context.packageManager
		return try {
			val appInfo = pm.getApplicationInfo(packageName, 0)
			appInfo.loadIcon(pm)
		} catch (_: PackageManager.NameNotFoundException) {
			Color.LTGRAY.toDrawable()
		}
	}
}
