package com.teambrake.brake.presentation.registry.model

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Stable
import com.teambrake.brake.core.appscanner.AppMetaData

@Stable
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
