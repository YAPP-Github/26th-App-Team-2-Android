package com.yapp.breake.presentation.registry.model

import android.graphics.drawable.Drawable
import com.yapp.breake.core.appscanner.AppMetaData

data class AppModel(
	val isSelected: Boolean,
	val name: String,
	val packageName: String,
	val icon: Drawable?,
) {
	companion object {
		val initialAppsMapper: (AppMetaData) -> AppModel = { appMetaData ->
			AppModel(
				isSelected = false,
				name = appMetaData.appName,
				packageName = appMetaData.packageName,
				icon = appMetaData.icon,
			)
		}
	}
}
