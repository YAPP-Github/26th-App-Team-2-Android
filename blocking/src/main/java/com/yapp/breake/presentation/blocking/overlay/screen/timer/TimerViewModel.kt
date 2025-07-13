package com.yapp.breake.presentation.blocking.overlay.screen.timer

import android.content.Context
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.presentation.blocking.scheduler.AlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TimerViewModel @Inject constructor() : ViewModel() {

	var time = mutableIntStateOf(0)
		private set

	private val _toastEffect: MutableSharedFlow<String> = MutableSharedFlow()
	val toastEffect: SharedFlow<String> get() = _toastEffect

	fun setBreakTimeAlarm(context: Context, groupId: Long) {
		AlarmScheduler.scheduleAlarm(
			context = context,
			minute = time.intValue,
			alarmId = groupId,
		).onSuccess {
			sendToastMessage("알람이 설정되었습니다. ${time.intValue} 분 후에 휴식 시간이 시작됩니다.")
		}.onFailure {
			sendToastMessage("알람 설정에 실패했습니다. 정확한 알람 권한을 확인해주세요.")
			it.printStackTrace()
		}
	}

	fun setTime(value: Int) {
		this.time.intValue = value
	}

	fun sendToastMessage(message: String) {
		viewModelScope.launch {
			_toastEffect.emit(message)
		}
	}
}
