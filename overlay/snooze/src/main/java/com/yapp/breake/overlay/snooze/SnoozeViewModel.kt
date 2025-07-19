package com.yapp.breake.overlay.snooze

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.SetAlarmUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SnoozeViewModel @Inject constructor(
	private val setAlarmUsecase: SetAlarmUsecase,
	private val appGroupRepository: AppGroupRepository,
) : ViewModel() {

	val isSnoozed = mutableStateOf(false)

	private val _toastEffect: MutableSharedFlow<String> = MutableSharedFlow()
	val toastEffect: SharedFlow<String> get() = _toastEffect

	fun setSnooze(groupId: Long) {
		viewModelScope.launch {
			setAlarmUsecase(
				groupId = groupId,
				appGroupState = AppGroupState.SnoozeBlocking(),
			).onSuccess {
				isSnoozed.value = true
			}.onFailure {
				sendToastMessage("알람 설정에 실패했습니다. 정확한 알람 권한을 확인해주세요.")
			}
		}
	}

	fun setAppGroupToBlocking(groupId: Long) {
		viewModelScope.launch {
			appGroupRepository.setAppGroupState(
				groupId = groupId,
				appGroupState = AppGroupState.Blocking,
			)
		}
	}

	fun sendToastMessage(message: String) {
		viewModelScope.launch {
			_toastEffect.emit(message)
		}
	}
}
