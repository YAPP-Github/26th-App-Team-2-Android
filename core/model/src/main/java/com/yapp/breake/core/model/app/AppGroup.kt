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

	companion object {
		val sample = AppGroup(
			id = 1L,
			name = "SNS",
			appGroupState = AppGroupState.NeedSetting,
			apps = listOf(
				App(
					packageName = "com.example.app1",
					category = "앱",
					name = "앱1",
					icon = null,
				),
			),
			snoozes = emptyList(),
		)
	}
}
