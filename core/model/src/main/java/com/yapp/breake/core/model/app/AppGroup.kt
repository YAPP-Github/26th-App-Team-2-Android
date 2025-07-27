package com.yapp.breake.core.model.app

data class AppGroup(
	val id: Long,
	val name: String,
	val appGroupState: AppGroupState,
	val apps: List<App>,
	val snoozes: List<Snooze>,
) {

	val snoozesCount: Int
		get() = snoozes.size
}
