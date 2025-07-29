package com.yapp.breake.core.appscanner

import android.graphics.drawable.Drawable

interface InstalledAppScanner {
	fun getInstalledAppsMetaData(): List<AppMetaData>
	fun getIconDrawable(packageName: String): Drawable
}
