package com.teambrake.brake.presentation.home

import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.android.Amplitude
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.amplitude.core.events.Identify
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.amplitude.CoffeechatActionType
import com.teambrake.brake.core.amplitude.CoffeechatTriggerSource
import com.teambrake.brake.core.datastore.model.DatastoreFeedback
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.usecase.SetBlockingAlarmUseCase
import com.teambrake.brake.presentation.home.contract.HomeEvent
import com.teambrake.brake.presentation.home.contract.HomeModalState
import com.teambrake.brake.presentation.home.contract.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
	appGroupRepository: AppGroupRepository,
	private val setBlockingAlarmUseCase: SetBlockingAlarmUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
	private val amplitude: Amplitude,
	private val feedbackDataStore: DataStore<DatastoreFeedback>,
) : ViewModel() {

	val homeUiState: StateFlow<HomeUiState> = appGroupRepository
		.observeAppGroup()
		.map(::returnHomeUiState)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = HomeUiState.Loading,
		)

	private val _homeModalState: MutableStateFlow<HomeModalState> =
		MutableStateFlow(HomeModalState.Nothing)
	val homeModalState: StateFlow<HomeModalState> get() = _homeModalState

	private val _homeEvent: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
	val homeEvent: MutableSharedFlow<HomeEvent> get() = _homeEvent

	init {
		// 홈 화면 진입 시 이벤트 트래킹
		trackHomeView()
		checkFeedbackPopup()
		observeSessionEnd()
	}

	private fun returnHomeUiState(appGroups: List<AppGroup>): HomeUiState {
		if (appGroups.isEmpty()) {
			return HomeUiState.Nothing
		}

		appGroups.forEach { appGroup ->
			when (appGroup.appGroupState) {
				AppGroupState.Blocking, AppGroupState.SnoozeBlocking, AppGroupState.Using -> {
					return HomeUiState.Ticking(
						appGroups.toPersistentList(),
					)
				}

				AppGroupState.NeedSetting -> {}
			}
		}

		return HomeUiState.GroupList(appGroups.toPersistentList())
	}

	fun showStopUsingDialog(appGroup: AppGroup) {
		_homeModalState.update {
			HomeModalState.StopUsingDialog(appGroup)
		}
		firebaseAnalytics.logEvent("try_stop_session") {
			param("where", "home_screen")
		}
	}

	fun stopAppUsing(appGroup: AppGroup) {
		viewModelScope.launch {
			val result = setBlockingAlarmUseCase(
				groupId = appGroup.id,
			)
			if (result is BrakeResult.Success) {
				showStopUsingSuccess(appGroup.name)
			}

			launch(Dispatchers.IO) {
				// 현재 세션 정보 계산
				val startTime = appGroup.startTime ?: java.time.LocalDateTime.now()
				val plannedDuration = appGroup.goalMinutes ?: 0
				val elapsedDuration = java.time.Duration.between(
					startTime,
					java.time.LocalDateTime.now(),
				).toMinutes().toInt()

				trackClickEarlyExit(
					plannedDuration = plannedDuration,
					elapsedDuration = elapsedDuration,
					appGroup = appGroup,
				)
			}
		}
	}

	private fun showStopUsingSuccess(groupName: String) {
		viewModelScope.launch {
			_homeEvent.emit(HomeEvent.ShowStopUsingSuccess(groupName))
		}
		firebaseAnalytics.logEvent("stop_session") {
			param("reason", "user_stop")
		}
	}

	fun navigateToRegistry(groupId: Long? = null) {
		viewModelScope.launch {
			_homeEvent.emit(HomeEvent.NavigateToRegistry(groupId))
		}
		if (groupId != null) {
			firebaseAnalytics.logEvent("try_edit_existed_group") {
				param("group_id", groupId)
			}
		} else {
			firebaseAnalytics.logEvent("try_add_new_group") {
				param("group_id", "null")
			}
		}
	}

	// ============ Coffeechat Popup Functions ============

	private fun checkFeedbackPopup() {
		viewModelScope.launch(Dispatchers.IO) {
			val feedback = feedbackDataStore.data.first()
			if (shouldShowFeedbackPopup(feedback)) {
				showFeedbackDialog(CoffeechatTriggerSource.APP_OPEN)
			}
		}
	}

	private fun observeSessionEnd() {
		viewModelScope.launch {
			homeUiState
				.map { it is HomeUiState.Ticking }
				.distinctUntilChanged()
				.collect { isTicking ->
					if (!isTicking) {
						val feedback = feedbackDataStore.data.first()
						if (shouldShowFeedbackPopup(feedback)) {
							showFeedbackDialog(CoffeechatTriggerSource.SESSION_END)
						}
					}
				}
		}
	}

	private fun shouldShowFeedbackPopup(feedback: DatastoreFeedback): Boolean {
		if (feedback.sessionCount < FeedbackConfig.REQUIRED_SESSION_COUNT) return false

		return when (feedback.status) {
			"" -> true
			"pending" -> {
				val daysSinceShown = (System.currentTimeMillis() - feedback.lastShownAt) / (1000 * 60 * 60 * 24)
				daysSinceShown >= FeedbackConfig.LATER_COOLDOWN_DAYS
			}
			"clicked" -> {
				val daysSinceShown = (System.currentTimeMillis() - feedback.lastShownAt) / (1000 * 60 * 60 * 24)
				daysSinceShown >= FeedbackConfig.ACCEPTED_COOLDOWN_DAYS
			}
			"rejected" -> false
			else -> false
		}
	}

	private fun showFeedbackDialog(triggerSource: CoffeechatTriggerSource) {
		_homeModalState.update { HomeModalState.FeedbackDialog }
		trackAmplitudeEvent(AmplitudeEventHelper.createViewCoffeechatPopupEvent(triggerSource))
	}

	fun onFeedbackAccept() {
		viewModelScope.launch(Dispatchers.IO) {
			feedbackDataStore.updateData {
				it.copy(status = "clicked", lastShownAt = System.currentTimeMillis())
			}
		}
		trackAmplitudeEvent(AmplitudeEventHelper.createClickCoffeechatPopupEvent(CoffeechatActionType.ACCEPT))
		trackCoffeechatStatus("clicked")
		_homeModalState.update { HomeModalState.Nothing }
	}

	fun onFeedbackLater() {
		viewModelScope.launch(Dispatchers.IO) {
			feedbackDataStore.updateData {
				it.copy(status = "pending", lastShownAt = System.currentTimeMillis())
			}
		}
		trackAmplitudeEvent(AmplitudeEventHelper.createClickCoffeechatPopupEvent(CoffeechatActionType.LATER))
		trackCoffeechatStatus("pending")
		_homeModalState.update { HomeModalState.Nothing }
	}

	fun onFeedbackReject() {
		viewModelScope.launch(Dispatchers.IO) {
			feedbackDataStore.updateData {
				it.copy(status = "rejected", lastShownAt = System.currentTimeMillis())
			}
		}
		trackAmplitudeEvent(AmplitudeEventHelper.createClickCoffeechatPopupEvent(CoffeechatActionType.REJECT))
		trackCoffeechatStatus("rejected")
		_homeModalState.update { HomeModalState.Nothing }
	}

	private fun trackCoffeechatStatus(status: String) {
		val userProperty = AmplitudeEventHelper.setCoffeechatStatus(status)
		amplitude.identify(
			Identify().apply {
				userProperty.toUserProperties().forEach { (key, value) ->
					set(key, value)
				}
			},
		)
	}

	// ============ Amplitude Event Tracking Functions ============

	/**
	 * Amplitude 이벤트 전송 공통 함수
	 */
	private fun trackAmplitudeEvent(event: com.teambrake.brake.core.amplitude.AmplitudeEvent) {
		amplitude.track(event.getEventName(), event.toEventProperties())
	}

	/**
	 * 4. view_home 이벤트 전송
	 */
	private fun trackHomeView() {
		viewModelScope.launch(Dispatchers.IO) {
			trackAmplitudeEvent(AmplitudeEventHelper.createViewHomeEvent())
		}
	}

	/**
	 * 5. click_early_exit 이벤트 전송
	 */
	private fun trackClickEarlyExit(
		plannedDuration: Int,
		elapsedDuration: Int,
		appGroup: AppGroup,
	) {
		trackAmplitudeEvent(
			AmplitudeEventHelper.createClickEarlyExitEvent(
				plannedDuration = plannedDuration,
				elapsedDuration = elapsedDuration,
				groupId = appGroup.id.toString(),
				groupName = appGroup.name,
				groupAppCount = appGroup.apps.size,
			),
		)
	}

	fun dismiss() {
		_homeModalState.update { HomeModalState.Nothing }
	}
}
