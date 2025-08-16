package com.yapp.breake.overlay.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.usecase.SetAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor(
	private val setAlarmUsecase: SetAlarmUseCase,
) : ViewModel() {

	private val _timerUiState = MutableStateFlow<TimerUiState>(TimerUiState.Init)
	val timerUiState: StateFlow<TimerUiState> get() = _timerUiState

	private val _toastEffect: MutableSharedFlow<String> = MutableSharedFlow()
	val toastEffect: SharedFlow<String> get() = _toastEffect

	fun setBreakTimeAlarm(groupId: Long, groupName: String) {
		val uiState = timerUiState.value as? TimerUiState.TimeSetting ?: return

		viewModelScope.launch {
			setAlarmUsecase(
				second = uiState.time,
				groupId = groupId,
				groupName = groupName,
				appGroupState = AppGroupState.Using,
			).onSuccess {
				confirmTime(uiState.time, it)
			}.onFailure {
				sendToastMessage("알람 설정에 실패했습니다. 정확한 알람 권한을 확인해주세요.")
			}
		}
	}

	fun initTimeSetting() {
		_timerUiState.update {
			TimerUiState.TimeSetting(10)
		}
	}

	fun setTime(value: Int = 10) {
		val uiState = timerUiState.value as? TimerUiState.TimeSetting ?: return
		_timerUiState.update {
			uiState.copy(
				time = value,
			)
		}
	}

	private fun confirmTime(durationMinutes: Int, endTime: LocalDateTime) {
		_timerUiState.update {
			TimerUiState.SetComplete(durationMinutes, endTime)
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
