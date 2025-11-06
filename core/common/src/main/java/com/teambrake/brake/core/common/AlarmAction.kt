package com.teambrake.brake.core.common

enum class AlarmAction {
	ACTION_USING,
	ACTION_BLOCKING, ;

	companion object {
		fun fromString(action: String?): AlarmAction = entries.find { it.name == action } ?: ACTION_USING
	}
}
