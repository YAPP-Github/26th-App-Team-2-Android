package com.teambrake.brake.core.auth.kakao

import android.content.Context
import android.content.pm.PackageManager

internal fun isKakaoInstalled(context: Context): Boolean = try {
	context.packageManager.getPackageInfo("com.kakao.talk", 0)
	true
} catch (_: PackageManager.NameNotFoundException) {
	false
}
