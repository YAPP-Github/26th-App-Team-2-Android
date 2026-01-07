package com.teambrake.brake.core.amplitude

enum class EventName(val eventName: String) {
	OPEN_APP("open_app"),
	VIEW_ONBOARDING("view_onboarding"),
	COMPLETE_ONBOARDING("complete_onboarding"),
	VIEW_HOME("view_home"),
	CLICK_EARLY_EXIT("click_early_exit"),
	CREATE_APP_GROUP("create_app_group"),
	EDIT_APP_GROUP("edit_app_group"),
	DELETE_APP_GROUP("delete_app_group"),
	VIEW_BLOCKING_START("view_blocking_start"),
	VIEW_BLOCKING_START_SET_TIME("view_blocking_start_set_time"),
	VIEW_BLOCKING_START_CONFIRM("view_blocking_start_confirm"),
	CLICK_SESSION_START("click_session_start"),
	VIEW_BLOCKING_FINISH("view_blocking_finish"),
	CLICK_SNOOZE("click_snooze"),
	END_SESSION("end_session"),
	VIEW_COOLDOWN("view_cooldown"),
	TOTAL_GROUP_COUNT("total_group_count"),
	IS_ONBOARDING_COMPLETED("is_onboarding_completed"),
	LAST_SESSION_DATE("last_session_date"),
}
