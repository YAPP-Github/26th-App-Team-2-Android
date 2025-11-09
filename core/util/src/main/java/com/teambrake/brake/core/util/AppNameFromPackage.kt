package com.teambrake.brake.core.util

import android.content.Context
import android.content.pm.PackageManager
import timber.log.Timber

fun Context.getAppNameFromPackage(packageName: String): String? = try {
	val packageManager = packageManager
	val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
	val appName = packageManager.getApplicationLabel(applicationInfo).toString()
	Timber.d("앱 이름을 찾았습니다: $packageName -> $appName")
	appName
} catch (e: PackageManager.NameNotFoundException) {
	Timber.w("패키지를 찾을 수 없습니다: $packageName, 기본 이름을 사용합니다")
	packageName.substringAfterLast('.').replaceFirstChar { it.uppercase() }
} catch (e: Exception) {
	Timber.e(e, "앱 이름을 가져오는 중 오류 발생: $packageName")
	packageName.substringAfterLast('.').replaceFirstChar { it.uppercase() }
}
