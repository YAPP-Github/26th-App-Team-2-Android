package com.yapp.breake.core.model.app

import java.time.LocalDateTime

data class AppGroup(
	val id: Long,
	val name: String,
	val appGroupState: AppGroupState,
	val apps: List<App>,
	val snoozes: List<Snooze>,
	val goalMinutes: Int?,
	val sessionStartTime: LocalDateTime?,
	val startTime: LocalDateTime?,
	val endTime: LocalDateTime?,
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
					id = 1L,
					category = "앱",
					name = "앱1",
					icon = null,
				),
			),
			snoozes = emptyList(),
			goalMinutes = 30,
			sessionStartTime = null,
			startTime = LocalDateTime.now().minusMinutes(2).minusSeconds(3),
			endTime = LocalDateTime.now().plusMinutes(12).plusSeconds(7),
		)
	}
}
