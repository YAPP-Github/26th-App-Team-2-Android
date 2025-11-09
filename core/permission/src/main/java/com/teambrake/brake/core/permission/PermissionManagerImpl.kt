package com.teambrake.brake.core.permission

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.AlarmManager
import android.app.AppOpsManager
import android.app.AppOpsManager.MODE_ALLOWED
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Process
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import jakarta.inject.Inject

class PermissionManagerImpl @Inject constructor() : PermissionManager {
	override fun isAllGranted(context: Context): Boolean {
		if (!isOverlayPermissionGranted(context)) return false
		if (!isStatsPermissionGranted(context)) return false
		if (!isExactAlarmPermissionGranted(context)) return false
		if (!isAccessibilityPermissionGranted(context)) return false
		return true
	}

	override fun isGranted(context: Context, permissionType: PermissionType): Boolean = when (permissionType) {
		PermissionType.OVERLAY -> isOverlayPermissionGranted(context)
		PermissionType.STATS -> isStatsPermissionGranted(context)
		PermissionType.EXACT_ALARM -> isExactAlarmPermissionGranted(context)
		PermissionType.ACCESSIBILITY -> isAccessibilityPermissionGranted(context)
	}

	private fun isOverlayPermissionGranted(context: Context): Boolean =
		Settings.canDrawOverlays(context)

	private fun isStatsPermissionGranted(context: Context): Boolean {
		// API 29 이상 부터 PACKAGE_USAGE_STATS 권한 설정 요구
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return true

		val appOpsManager =
			context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
		// API 29 이상에서 unsafeCheckOpNoThrow 사용
		val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			appOpsManager.unsafeCheckOpNoThrow(
				"android:get_usage_stats",
				Process.myUid(),
				context.packageName,
			)
		} else {
			@Suppress("DEPRECATION")
			appOpsManager.checkOpNoThrow(
				"android:get_usage_stats",
				Process.myUid(),
				context.packageName,
			)
		}
		return mode == MODE_ALLOWED
	}

	private fun isExactAlarmPermissionGranted(context: Context): Boolean {
		// API 31 이상 부터 SCHEDULE_EXACT_ALARM 권한 설정 요구
		// API 33 미만은 Manifest에 선언된 경우 자동으로 허용됨
		// API 33 이상에서는 Manifest에 선언되어 있어도 사용자가 명시적으로 허용해야 함
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			alarmManager.canScheduleExactAlarms()
		} else {
			true
		}
	}

	private fun isAccessibilityPermissionGranted(context: Context): Boolean {
		val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
		return am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
			.any { it.resolveInfo.serviceInfo.packageName == context.packageName }
	}

	override fun getIntent(context: Context, permissionType: PermissionType): Intent =
		when (permissionType) {
			PermissionType.OVERLAY -> getOverlayIntent(context)
			PermissionType.STATS -> getStatsIntent(context)
			PermissionType.EXACT_ALARM -> getExactAlarmIntent(context)
			PermissionType.ACCESSIBILITY -> getAccessibilityIntent()
		}

	private fun getOverlayIntent(context: Context): Intent =
		Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
			data = Uri.fromParts("package", context.packageName, null)
		}

	private fun getStatsIntent(context: Context): Intent =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS).apply {
				data = Uri.fromParts("package", context.packageName, null)
			}
		} else {
			// 이전 버전에서는 PACKAGE_USAGE_STATS 권한이 필요하지 않아 비어있는 Intent 반환
			Intent()
		}

	private fun getExactAlarmIntent(context: Context): Intent =
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
				data = Uri.fromParts("package", context.packageName, null)
			}
		} else {
			// 이전 버전에서는 SCHEDULE_EXACT_ALARM 권한이 필요하지 않아 비어있는 Intent 반환
			Intent()
		}

	private fun getAccessibilityIntent(): Intent =
		Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
}
