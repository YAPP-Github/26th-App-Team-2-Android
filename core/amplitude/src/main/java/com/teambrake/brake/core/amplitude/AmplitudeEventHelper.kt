package com.teambrake.brake.core.amplitude

/**
 * Amplitude 이벤트 생성을 위한 헬퍼 클래스
 * 타입 안정성을 제공하면서도 확장 가능한 구조 유지
 */
object AmplitudeEventHelper {

	/**
	 * 1. open_app 이벤트 생성
	 * @param triggerSource 앱 실행 트리거 소스
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createOpenAppEvent(
		triggerSource: TriggerSource,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.OPEN_APP,
		parameters = mapOf(
			EventParameter.TRIGGER_SOURCE to triggerSource.value,
		),
	)

	/**
	 * 2. view_onboarding 이벤트 생성
	 * @param stepName 온보딩 단계 이름
	 * @param stepDetail 온보딩 단계 세부사항
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewOnboardingEvent(
		stepName: StepName,
		stepDetail: StepDetail,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_ONBOARDING,
		parameters = mapOf(
			EventParameter.STEP_NAME to stepName.value,
			EventParameter.STEP_DETAIL to stepDetail.value,
		),
	)

	/**
	 * 3. complete_onboarding 이벤트 생성
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createCompleteOnboardingEvent(): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.COMPLETE_ONBOARDING,
		parameters = emptyMap(),
	)

	/**
	 * 4. view_home 이벤트 생성
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewHomeEvent(): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_HOME,
		parameters = emptyMap(),
	)

	/**
	 * 5. click_early_exit 이벤트 생성
	 * @param plannedDuration 계획된 시간 (분)
	 * @param elapsedDuration 경과된 시간 (분)
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createClickEarlyExitEvent(
		plannedDuration: Int,
		elapsedDuration: Int,
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.CLICK_EARLY_EXIT,
		parameters = mapOf(
			EventParameter.PLANNED_DURATION to plannedDuration,
			EventParameter.ELAPSED_DURATION to elapsedDuration,
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 6. create_app_group 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createAppGroupEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.CREATE_APP_GROUP,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 7. edit_app_group 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun editAppGroupEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.EDIT_APP_GROUP,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 8. delete_app_group 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun deleteAppGroupEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.DELETE_APP_GROUP,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 9. view_blocking_start 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewBlockingStartEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_BLOCKING_START,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 10. view_blocking_start_set_time 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewBlockingStartSetTimeEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_BLOCKING_START_SET_TIME,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 11. view_blocking_start_confirm 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @param plannedDuration 계획된 시간 (분)
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewBlockingStartConfirmEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
		plannedDuration: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_BLOCKING_START_CONFIRM,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
			EventParameter.PLANNED_DURATION to plannedDuration,
		),
	)

	/**
	 * 12. click_session_start 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @param plannedDuration 계획된 시간 (분)
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createClickSessionStartEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
		plannedDuration: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.CLICK_SESSION_START,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
			EventParameter.PLANNED_DURATION to plannedDuration,
		),
	)

	/**
	 * 13. view_blocking_finish 이벤트 생성
	 * @param groupId 그룹 ID
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewBlockingFinishEvent(
		groupId: String,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_BLOCKING_FINISH,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
		),
	)

	/**
	 * 14. click_snooze 이벤트 생성
	 * @param snoozeNth 스누즈 횟수 (0, 1, 2)
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createClickSnoozeEvent(
		snoozeNth: Int,
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.CLICK_SNOOZE,
		parameters = mapOf(
			EventParameter.SNOOZE_NTH to snoozeNth,
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 15. end_session 이벤트 생성
	 * @param plannedDuration 계획된 시간 (분)
	 * @param elapsedDuration 경과된 시간 (분)
	 * @param snoozeCount 스누즈 횟수 (0, 1, 2)
	 * @param isEarlyExit 조기 종료 여부
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createEndSessionEvent(
		plannedDuration: Int,
		elapsedDuration: Int,
		snoozeCount: Int,
		isEarlyExit: Boolean,
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.END_SESSION,
		parameters = mapOf(
			EventParameter.PLANNED_DURATION to plannedDuration,
			EventParameter.ELAPSED_DURATION to elapsedDuration,
			EventParameter.SNOOZE_COUNT to snoozeCount,
			EventParameter.IS_EARLY_EXIT to isEarlyExit,
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 16. view_cooldown 이벤트 생성
	 * @param groupId 그룹 ID
	 * @param groupName 그룹 이름
	 * @param groupAppCount 그룹 앱 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createViewCooldownEvent(
		groupId: String,
		groupName: String,
		groupAppCount: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.VIEW_COOLDOWN,
		parameters = mapOf(
			EventParameter.GROUP_ID to groupId,
			EventParameter.GROUP_NAME to groupName,
			EventParameter.GROUP_APP_COUNT to groupAppCount,
		),
	)

	/**
	 * 17. total_group_count 이벤트 생성 (User Property)
	 * @param count 총 그룹 개수
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createTotalGroupCountEvent(
		count: Int,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.TOTAL_GROUP_COUNT,
		parameters = mapOf(
			"count" to count,
		),
	)

	/**
	 * 18. is_onboarding_completed 이벤트 생성 (User Property)
	 * @param isCompleted 온보딩 완료 여부
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createIsOnboardingCompletedEvent(
		isCompleted: Boolean,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.IS_ONBOARDING_COMPLETED,
		parameters = mapOf(
			"is_completed" to isCompleted,
		),
	)

	/**
	 * 19. last_session_date 이벤트 생성 (User Property)
	 * @param date 마지막 세션 날짜 (YYYY-MM-DD)
	 * @return 이벤트 이름과 파라미터 맵
	 */
	fun createLastSessionDateEvent(
		date: String,
	): AmplitudeEvent = AmplitudeEvent(
		eventName = EventName.LAST_SESSION_DATE,
		parameters = mapOf(
			"date" to date,
		),
	)
}

/**
 * Amplitude 이벤트 데이터 클래스
 */
data class AmplitudeEvent(
	val eventName: EventName,
	val parameters: Map<Any, Any>,
) {
	/**
	 * Amplitude SDK에 전달할 수 있는 형태로 변환
	 */
	fun toEventProperties(): Map<String, Any> = parameters.mapKeys {
		when (val key = it.key) {
			is EventParameter -> key.parameterName
			else -> key.toString()
		}
	}

	/**
	 * 이벤트 이름을 문자열로 반환
	 */
	fun getEventName(): String = eventName.eventName
}
