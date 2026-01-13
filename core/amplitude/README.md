# Amplitude Event Tracking

Amplitude 이벤트 추적을 위한 헬퍼 함수와 상수 정의

## 📖 빠른 시작

### Track 이벤트 사용법

```kotlin
// 1. 헬퍼 함수로 이벤트 생성
val event = AmplitudeEventHelper.createOpenAppEvent(TriggerSource.APP_ICON)

// 2. Amplitude SDK로 추적
amplitude.track(event.getEventName(), event.toEventProperties())
```

### User Property 설정

```kotlin
// 1. 헬퍼 함수로 프로퍼티 생성
val property = AmplitudeEventHelper.setTotalGroupCount(count = 5)

// 2. Amplitude SDK로 설정
amplitude.identify(Identify().apply {
    property.toUserProperties().forEach { (key, value) -> set(key, value) }
})
```

---

## 📋 이벤트 목록

| # | 이벤트명 | 설명 | 파라미터 | 헬퍼 함수 |
|---|---------|------|--------|---------|
| 1 | `open_app` | 앱 시작 | `trigger_source` | `createOpenAppEvent()` |
| 2 | `view_onboarding` | 온보딩 화면 진입 | `step_name`, `step_detail` | `createViewOnboardingEvent()` |
| 3 | `complete_onboarding` | 온보딩 완료 | - | `createCompleteOnboardingEvent()` |
| 4 | `view_home` | 홈 화면 진입 | - | `createViewHomeEvent()` |
| 5 | `click_early_exit` | 세션 조기 종료 클릭 | `planned_duration`, `elapsed_duration`, `group_*` | `createClickEarlyExitEvent()` |
| 6 | `create_app_group` | 앱 그룹 생성 | `group_id`, `group_name`, `group_app_count` | `createAppGroupEvent()` |
| 7 | `edit_app_group` | 앱 그룹 수정 | `group_id`, `group_name`, `group_app_count` | `editAppGroupEvent()` |
| 8 | `delete_app_group` | 앱 그룹 삭제 | `group_id`, `group_name`, `group_app_count` | `deleteAppGroupEvent()` |
| 9 | `view_blocking_start` | 차단 시작 화면 진입 | `group_*` | `createViewBlockingStartEvent()` |
| 10 | `view_blocking_start_set_time` | 차단 시간 설정 화면 진입 | `group_*` | `createViewBlockingStartSetTimeEvent()` |
| 11 | `view_blocking_start_confirm` | 차단 확인 화면 진입 | `group_*`, `planned_duration` | `createViewBlockingStartConfirmEvent()` |
| 12 | `click_brake_session_start` | 세션 시작 클릭 | `group_*`, `planned_duration` | `createClickBrakeSessionStartEvent()` |
| 13 | `view_blocking_finish` | 차단 완료 화면 진입 | `group_id` | `createViewBlockingFinishEvent()` |
| 14 | `click_snooze` | 스누즈 클릭 | `snooze_nth`, `group_*` | `createClickSnoozeEvent()` |
| 15 | `end_brake_session` | 세션 종료 | `planned_duration`, `elapsed_duration`, `snooze_count`, `is_early_exit`, `group_*` | `createEndBrakeSessionEvent()` |
| 16 | `view_cooldown` | 쿨다운 화면 진입 | `group_*` | `createViewCooldownEvent()` |
| 17 | `total_group_count` | 총 그룹 개수 (User Property) | int | `setTotalGroupCount()` |
| 18 | `is_onboarding_completed` | 온보딩 완료 여부 (User Property) | boolean | `setIsOnboardingCompleted()` |
| 19 | `last_brake_session_date` | 마지막 세션 날짜 (User Property) | string (YYYY-MM-DD) | `setLastBrakeSessionDate()` |

---

## 🔍 주요 파라미터 값

### TriggerSource
- `APP_ICON`: 앱 아이콘으로 실행
- `SHIELD_OVERLAY`: 쉴드 오버레이로 실행
- `NOTIFICATION`: 알림으로 실행

### StepName (온보딩 단계)
- `LOGIN`, `NICKNAME`, `TUTORIAL`, `PERMISSION`, `WELCOME`

### StepDetail (온보딩 세부사항)
- `DEFAULT`, `STEP1`, `STEP2`, `STEP3`, `OVERLAY`, `USAGE`, `NOTIFICATION`, `ACCESSIBILITY`

### Group 파라미터
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

---

## 🔧 새 이벤트 추가 방법

1. `EventName.kt`: 이벤트 이름 enum에 추가
2. `EventParameter.kt`: 필요한 파라미터 enum에 추가
3. `AmplitudeEventHelper.kt`: 헬퍼 함수 추가
4. `README.md`: 테이블에 추가

---

## 📝 사용 예시

```kotlin
// 1. 앱 오픈 이벤트
amplitude.track(
    AmplitudeEventHelper.createOpenAppEvent(TriggerSource.APP_ICON).getEventName(),
    AmplitudeEventHelper.createOpenAppEvent(TriggerSource.APP_ICON).toEventProperties()
)

// 2. 온보딩 추적
amplitude.track(
    AmplitudeEventHelper.createViewOnboardingEvent(
        stepName = StepName.TUTORIAL,
        stepDetail = StepDetail.STEP1
    ).getEventName(),
    AmplitudeEventHelper.createViewOnboardingEvent(
        stepName = StepName.TUTORIAL,
        stepDetail = StepDetail.STEP1
    ).toEventProperties()
)

// 3. 세션 종료 추적
amplitude.track(
    AmplitudeEventHelper.createEndBrakeSessionEvent(
        plannedDuration = 60,
        elapsedDuration = 45,
        snoozeCount = 1,
        isEarlyExit = false,
        groupId = "group_123",
        groupName = "업무 앱",
        groupAppCount = 5
    ).getEventName(),
    AmplitudeEventHelper.createEndBrakeSessionEvent(
        plannedDuration = 60,
        elapsedDuration = 45,
        snoozeCount = 1,
        isEarlyExit = false,
        groupId = "group_123",
        groupName = "업무 앱",
        groupAppCount = 5
    ).toEventProperties()
)

// 4. User Property 설정
amplitude.identify(
    Identify().apply {
        AmplitudeEventHelper.setTotalGroupCount(count = 3)
            .toUserProperties()
            .forEach { (key, value) -> set(key, value) }
    }
)
```

