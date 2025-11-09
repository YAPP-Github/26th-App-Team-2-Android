package com.teambrake.brake.core.database.converter

import androidx.room.TypeConverter
import com.teambrake.brake.core.model.app.AppGroupState

internal class AppGroupStateConverter {

	@TypeConverter
	fun fromAppGroupState(appGroupState: AppGroupState): String = appGroupState.name

	@TypeConverter
	fun toAppGroupState(value: String): AppGroupState = AppGroupState.valueOf(value)
}
