package com.teambrake.brake.core.amplitude

enum class EventParameter(val parameterName: String) {
	TRIGGER_SOURCE("trigger_source"),
	STEP_NAME("step_name"),
	STEP_DETAIL("step_detail"),
	PLANNED_DURATION("planned_duration"),
	ELAPSED_DURATION("elapsed_duration"),
	GROUP_ID("group_id"),
	GROUP_NAME("group_name"),
	GROUP_APP_COUNT("group_app_count"),
	SNOOZE_NTH("snooze_nth"),
	SNOOZE_COUNT("snooze_count"),
	IS_EARLY_EXIT("is_early_exit"),
}

/**
 * Event Parameter Values for TRIGGER_SOURCE
 */
enum class TriggerSource(val value: String) {
	APP_ICON("app_icon"),
	SHIELD_OVERLAY("shield_overlay"),
	NOTIFICATION("notification"),
}

/**
 * Event Parameter Values for STEP_NAME
 */
enum class StepName(val value: String) {
	LOGIN("login"),
	NICKNAME("nickname"),
	TUTORIAL("tutorial"),
	PERMISSION("permission"),
	WELCOME("welcome"),
}

/**
 * Event Parameter Values for STEP_DETAIL
 */
enum class StepDetail(val value: String) {
	DEFAULT("default"),
	STEP1("step1"),
	STEP2("step2"),
	STEP3("step3"),
	OVERLAY("overlay"),
	USAGE("usage"),
	NOTIFICATION("notification"),
	ACCESSIBILITY("accessibility"),
}
