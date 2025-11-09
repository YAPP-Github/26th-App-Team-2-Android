package com.teambrake.brake.core.model.accessibility

object IntentConfig {
	// 동적 BroadcastReceiver 등록 시 키로 등록하며,
	const val RECEIVER_IDENTITY = "com.teambrake.brake.core.model.accessibility.ACCESSIBILITY_SERVICE_INTENT_FILTER"
	const val EXTRA_GROUP_ID = "extra_group_id"
	const val EXTRA_GROUP_STATE = "extra_group_state"
	const val EXTRA_SNOOZES_COUNT = "extra_snoozes_count"
}
