package com.yapp.breake.overlay.snooze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.common.Constants
import com.yapp.breake.domain.usecaseImpl.SetSnoozeAlarmUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SnoozeViewModel @Inject constructor(
    private val setSnoozeAlarmUsecase: SetSnoozeAlarmUsecase,
) : ViewModel() {

	private val _toastEffect: MutableSharedFlow<String> = MutableSharedFlow()
	val toastEffect: SharedFlow<String> get() = _toastEffect

	fun setSnooze(groupId: Long, appName: String) {
		viewModelScope.launch {
			setSnoozeAlarmUsecase(
				groupId = groupId,
				appName = appName,
			).onSuccess {
				sendToastMessage("알람이 ${Constants.SNOOZE_TIME}초 후에 다시 울립니다.")
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
