package com.teambrake.brake.overlay.snooze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teambrake.brake.domain.model.result.BrakeResult
import com.amplitude.android.Amplitude
import com.teambrake.brake.core.amplitude.AmplitudeEvent
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.domain.usecase.SetSnoozeAlarmUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SnoozeViewModel.SnoozeFactory::class)
internal class SnoozeViewModel @AssistedInject constructor(
	private val setSnoozeAlarmUsecase: SetSnoozeAlarmUseCase,
	private val amplitude: Amplitude,
	@Assisted private val groupId: Long,
	@Assisted private val groupName: String,
	@Assisted private val groupAppCount: Int,
) : ViewModel() {

	// Hilt가 이 Factory의 구현체를 알아서 만듦
	@AssistedFactory
	interface SnoozeFactory {
		fun create(
			groupId: Long,
			groupName: String,
			groupAppCount: Int,
		): SnoozeViewModel
	}

	private val _toastEffect: MutableSharedFlow<String> = MutableSharedFlow()
	val toastEffect: SharedFlow<String> get() = _toastEffect

	// ============ Amplitude Event Tracking Functions ============

	/**
	 * Amplitude 이벤트 전송 공통 함수
	 */
	private fun trackAmplitudeEvent(event: AmplitudeEvent) {
		amplitude.track(event.getEventName(), event.toEventProperties())
	}

	/**
	 * 9. view_blocking_finish 이벤트 전송
	 * 시간이 끝나고 스누즈 화면(그만하기/5분 더하기)이 나타날 때 트리거
	 */
	fun trackViewBlockingFinish() {
		viewModelScope.launch(Dispatchers.IO) {
			trackAmplitudeEvent(
				AmplitudeEventHelper.createViewBlockingFinishEvent(groupId = groupId.toString()),
			)
		}
	}

	fun setSnooze(snoozeNth: Int) {
		viewModelScope.launch {
			when (
				setSnoozeAlarmUsecase(
					groupId = groupId,
					groupName = groupName,
				)
			) {
				is BrakeResult.Error -> {
					sendToastMessage("알람 설정에 실패했습니다. 정확한 알람 권한을 확인해주세요.")
				}

				is BrakeResult.Success -> {
					/**
					 * 14. click_snooze 이벤트 전송
					 * 스누즈 버튼 클릭 시 호출 (그룹 정보와 스누즈 횟수 필요)
					 */
					launch(Dispatchers.IO) {
						trackAmplitudeEvent(
							AmplitudeEventHelper.createClickSnoozeEvent(
								snoozeNth = snoozeNth,
								groupId = groupId.toString(),
								groupName = groupName,
								groupAppCount = groupAppCount,
							),
						)
					}
				}
			}
		}
	}

	/**
	 * 15. view_cooldown 이벤트 전송
	 * 쿨다운 화면이 나타날 때 트리거
	 */
	fun trackViewCoolDown() {
		viewModelScope.launch(Dispatchers.IO) {
			trackAmplitudeEvent(
				AmplitudeEventHelper.createViewCooldownEvent(
					groupId = groupId.toString(),
					groupName = groupName,
					groupAppCount = groupAppCount,
				),
			)
		}
	}

	private fun sendToastMessage(message: String) {
		viewModelScope.launch {
			_toastEffect.emit(message)
		}
	}
}
