package com.yapp.breake.core.util

import android.content.Context
import android.content.pm.PackageManager
import timber.log.Timber

fun Context.getAppNameFromPackage(packageName: String): String? {
	return try {
		val packageManager = packageManager
		val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
		packageManager.getApplicationLabel(applicationInfo).toString()
	} catch (e: PackageManager.NameNotFoundException) {
		Timber.w("패키지를 찾을 수 없습니다: $packageName")
		null
	}
}
