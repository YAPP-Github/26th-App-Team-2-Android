package com.teambrake.brake.core.appscanner

import android.graphics.drawable.Drawable

data class AppMetaData(
	val appName: String,
	val packageName: String,
	val icon: Drawable? = null,
)
