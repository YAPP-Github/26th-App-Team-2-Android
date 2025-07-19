package com.yapp.breake.core.model.app

sealed interface AppGroupState {
	data object NeedSetting : AppGroupState
	data class SnoozeBlocking(val snoozeCount: Int = 0) : AppGroupState
	data object Blocking : AppGroupState
	data object Using : AppGroupState

	companion object {
		val AppGroupState.name: String
			get() = when (this) {
				NeedSetting -> "NeedSetting"
				is SnoozeBlocking -> "SnoozeBlocking"
				Blocking -> "Blocking"
				Using -> "Using"
			}

		fun fromName(name: String): AppGroupState {
			return when (name) {
				"NeedSetting" -> NeedSetting
				"SnoozeBlocking" -> SnoozeBlocking(0)
				"Blocking" -> Blocking
				"Using" -> Using
				else -> throw IllegalArgumentException("Unknown AppGroupState name: $name")
			}
		}
	}
}
