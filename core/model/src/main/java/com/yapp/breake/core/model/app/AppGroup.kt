package com.yapp.breake.core.model.app

data class AppGroup(
	val id: Long,
	val name: String,
	val apps: List<App>,
	val snoozes: List<Snooze>,
)
