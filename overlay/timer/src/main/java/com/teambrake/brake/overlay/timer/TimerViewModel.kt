package com.teambrake.brake.overlay.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.android.Amplitude
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.usecase.SetAlarmUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime

@HiltViewModel(assistedFactory = TimerViewModel.TimerFactory::class)
internal class TimerViewModel @AssistedInject constructor(
	private val setAlarmUsecase: SetAlarmUseCase,
	private val amplitude: Amplitude,
	@Assisted private val groupId: Long,
	@Assisted private val groupName: String,
	@Assisted private val groupAppCount: Int,
) : ViewModel() {

	// Hilt가 이 Factory의 구현체를 알아서 만듦
	@AssistedFactory
	interface TimerFactory {
		fun create(
			groupId: Long,
			groupName: String,
			groupAppCount: Int,
		): TimerViewModel
	}

	init {
		Timber.d("TimerViewModel created with groupId: $groupId, groupName: $groupName, groupAppCount: $groupAppCount")
	}

	private val _timerUiState = MutableStateFlow<TimerUiState>(TimerUiState.Init)
	val timerUiState: StateFlow<TimerUiState> get() = _timerUiState

	private val _toastEffect: MutableSharedFlow<String> = MutableSharedFlow()
	val toastEffect: SharedFlow<String> get() = _toastEffect

	fun resetToInitialState() {
		_timerUiState.update {
			when (it) {
				is TimerUiState.TimeSetting -> TimerUiState.Init
				else -> it
			}
		}
	}

	fun initTimeSetting() {
		viewModelScope.launch {
			_timerUiState.update {
				TimerUiState.TimeSetting(10)
			}

			/**
			 * 10. view_blocking_start_set_time 이벤트 전송
			 * 시간 설정 화면 진입 시 호출 (그룹 정보 필요)
			 */
			launch(Dispatchers.IO) {
				val event = AmplitudeEventHelper.createViewBlockingStartSetTimeEvent(
					groupId = groupId.toString(),
					groupName = groupName,
					groupAppCount = groupAppCount,
				)
				amplitude.track(event.getEventName(), event.toEventProperties())
			}
		}
	}

	fun changeTime(value: Int = 10) {
		val uiState = timerUiState.value as TimerUiState.TimeSetting
		_timerUiState.update {
			uiState.copy(
				time = value,
			)
		}
	}

	fun setTime() {
		viewModelScope.launch {
			val uiState = timerUiState.value as TimerUiState.TimeSetting

			val startTime = LocalDateTime.now()
			val triggerTime = startTime.plusSeconds((uiState.time * 60).toLong())
			_timerUiState.update {
				TimerUiState.SetComplete(uiState.time, triggerTime)
			}

			/**
			 * 11. view_blocking_start_confirm 이벤트 전송
			 * 시간 설정 확인 화면 진입 시 호출
			 */
			launch(Dispatchers.IO) {
				val event = AmplitudeEventHelper.createViewBlockingStartConfirmEvent(
					groupId = groupId.toString(),
					groupName = groupName,
					groupAppCount = groupAppCount,
					plannedDuration = uiState.time,
				)
				amplitude.track(event.getEventName(), event.toEventProperties())
			}
		}
	}

	fun confirmBreakTimeAlarm() {
		viewModelScope.launch {
			val uiState = timerUiState.value as TimerUiState.SetComplete

			setAlarmUsecase(
				second = uiState.durationMinutes,
				groupId = groupId,
				groupName = groupName,
				appGroupState = AppGroupState.Using,
			).onSuccess { endTime ->

				/**
				 * 12. click_brake_session_start 이벤트 전송
				 * 세션 시작 시 호출 (그룹 정보 필요)
				 */
				launch(Dispatchers.IO) {
					val startEvent = AmplitudeEventHelper.createClickBrakeSessionStartEvent(
						groupId = groupId.toString(),
						groupName = groupName,
						groupAppCount = groupAppCount,
						plannedDuration = uiState.durationMinutes,
					)
					amplitude.track(startEvent.getEventName(), startEvent.toEventProperties())
				}
			}.onFailure {
				sendToastMessage("알람 설정에 실패했습니다. 정확한 알람 권한을 확인해주세요.")
			}
		}
	}

	private fun sendToastMessage(message: String) {
		viewModelScope.launch {
			_toastEffect.emit(message)
		}
	}
}

sealed interface TimerUiState {
	data object Init : TimerUiState
	data class TimeSetting(val time: Int) : TimerUiState
	data class SetComplete(
		val durationMinutes: Int,
		val endTime: LocalDateTime,
	) : TimerUiState
}
