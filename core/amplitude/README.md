# Amplitude Event Tracking

Amplitude 이벤트 추적을 위한 상수 및 헬퍼 함수 정의

## 📖 사용 방법

### 1. 헬퍼 함수 사용 (권장)

헬퍼 함수를 사용하면 타입 안정성을 보장받으면서 쉽게 이벤트를 생성할 수 있습니다.

```kotlin
// open_app 이벤트
val event = AmplitudeEventHelper.createOpenAppEvent(
    triggerSource = TriggerSource.APP_ICON
)

// Amplitude SDK에 전달
amplitude.track(
    event.getEventName(),
    event.toEventProperties()
)
```

```kotlin
// view_onboarding 이벤트
val event = AmplitudeEventHelper.createViewOnboardingEvent(
    stepName = StepName.LOGIN,
    stepDetail = StepDetail.DEFAULT
)

amplitude.track(
    event.getEventName(),
    event.toEventProperties()
)
```

### 2. 직접 사용 (유연성이 필요한 경우)

특별한 경우에는 상수를 직접 사용할 수 있습니다.

```kotlin
amplitude.track(
    EventName.OPEN_APP.eventName,
    mapOf(
        EventParameter.TRIGGER_SOURCE.parameterName to TriggerSource.SHIELD_OVERLAY.value
    )
)
```

## 📋 정의된 이벤트

### 1. open_app
앱이 열릴 때 발생하는 이벤트

**파라미터:**
- `trigger_source` (string): 앱 실행 트리거
  - `app_icon`: 앱 아이콘으로 실행
  - `shield_overlay`: 쉴드 오버레이로 실행
  - `notification`: 알림으로 실행

**사용 예:**
```kotlin
AmplitudeEventHelper.createOpenAppEvent(TriggerSource.APP_ICON)
```

### 2. view_onboarding
온보딩 화면을 볼 때 발생하는 이벤트

**파라미터:**
- `step_name` (string): 온보딩 단계 이름
  - `login`: 로그인
  - `nickname`: 닉네임 설정
  - `tutorial`: 튜토리얼
  - `permission`: 권한 요청
  - `welcome`: 환영 화면

- `step_detail` (string): 온보딩 단계 세부사항
  - `default`: 기본
  - `step1`, `step2`, `step3`: 단계별
  - `overlay`: 오버레이
  - `usage`: 사용량
  - `notification`: 알림
  - `accessibility`: 접근성

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewOnboardingEvent(
    stepName = StepName.TUTORIAL,
    stepDetail = StepDetail.STEP1
)
```

### 3. complete_onboarding
온보딩 완료 시 발생하는 이벤트

**파라미터:** 없음

**사용 예:**
```kotlin
AmplitudeEventHelper.createCompleteOnboardingEvent()
```

### 4. view_home
홈 화면 진입 시 발생하는 이벤트

**파라미터:** 없음

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewHomeEvent()
```

### 5. click_early_exit
세션 조기 종료 버튼 클릭 시 발생하는 이벤트

**파라미터:**
- `planned_duration` (int): 계획된 시간 (분)
- `elapsed_duration` (int): 경과된 시간 (분)
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createClickEarlyExitEvent(
    plannedDuration = 60,
    elapsedDuration = 30,
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 6. create_app_group
앱 그룹 생성 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createAppGroupEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 7. edit_app_group
앱 그룹 수정 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.editAppGroupEvent(
    groupId = "group_123",
    groupName = "업무 앱 (수정)",
    groupAppCount = 7
)
```

### 8. delete_app_group
앱 그룹 삭제 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.deleteAppGroupEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 9. view_blocking_start
차단 시작 화면 진입 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewBlockingStartEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 10. view_blocking_start_set_time
차단 시작 시간 설정 화면 진입 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewBlockingStartSetTimeEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 11. view_blocking_start_confirm
차단 시작 확인 화면 진입 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수
- `planned_duration` (int): 계획된 시간 (분)

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewBlockingStartConfirmEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5,
    plannedDuration = 60
)
```

### 12. click_session_start
세션 시작 버튼 클릭 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수
- `planned_duration` (int): 계획된 시간 (분)

**사용 예:**
```kotlin
AmplitudeEventHelper.createClickSessionStartEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5,
    plannedDuration = 60
)
```

### 13. view_blocking_finish
차단 완료 화면 진입 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewBlockingFinishEvent(
    groupId = "group_123"
)
```

### 14. click_snooze
스누즈 버튼 클릭 시 발생하는 이벤트

**파라미터:**
- `snooze_nth` (int): 스누즈 횟수 (0, 1, 2)
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createClickSnoozeEvent(
    snoozeNth = 1,
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 15. end_session
세션 종료 시 발생하는 이벤트

**파라미터:**
- `planned_duration` (int): 계획된 시간 (분)
- `elapsed_duration` (int): 경과된 시간 (분)
- `snooze_count` (int): 스누즈 횟수 (0, 1, 2)
- `is_early_exit` (boolean): 조기 종료 여부
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createEndSessionEvent(
    plannedDuration = 60,
    elapsedDuration = 45,
    snoozeCount = 1,
    isEarlyExit = false,
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 16. view_cooldown
쿨다운 화면 진입 시 발생하는 이벤트

**파라미터:**
- `group_id` (string): 그룹 ID
- `group_name` (string): 그룹 이름
- `group_app_count` (int): 그룹 내 앱 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createViewCooldownEvent(
    groupId = "group_123",
    groupName = "업무 앱",
    groupAppCount = 5
)
```

### 17. total_group_count (User Property)
총 그룹 개수를 추적하는 사용자 속성

**파라미터:**
- `count` (int): 총 그룹 개수

**사용 예:**
```kotlin
AmplitudeEventHelper.createTotalGroupCountEvent(
    count = 3
)
```

### 18. is_onboarding_completed (User Property)
온보딩 완료 여부를 추적하는 사용자 속성

**파라미터:**
- `is_completed` (boolean): 온보딩 완료 여부

**사용 예:**
```kotlin
AmplitudeEventHelper.createIsOnboardingCompletedEvent(
    isCompleted = true
)
```

### 19. last_session_date (User Property)
마지막 세션 날짜를 추적하는 사용자 속성

**파라미터:**
- `date` (string): 마지막 세션 날짜 (YYYY-MM-DD 형식)

**사용 예:**
```kotlin
AmplitudeEventHelper.createLastSessionDateEvent(
    date = "2026-01-06"
)
```

## 🔧 새 이벤트 추가하기

1. `EventName.kt`에 이벤트 이름 추가
2. `EventParameter.kt`에 파라미터와 값 추가
3. `AmplitudeEventHelper.kt`에 헬퍼 함수 추가 (선택사항)
4. 이 README에 문서화

## 💡 설계 원칙

- **느슨한 연결**: 이벤트와 파라미터는 독립적으로 정의되어 재사용 가능
- **타입 안정성**: 헬퍼 함수로 컴파일 타임 검증 제공
- **확장성**: 새로운 이벤트나 파라미터 추가가 쉬움
- **유연성**: 필요시 직접 조합하여 사용 가능

