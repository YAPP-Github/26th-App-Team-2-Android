package com.teambrake.brake.overlay.blocking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.android.Amplitude
import com.teambrake.brake.core.amplitude.AmplitudeEvent
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = BlockingViewModel.BlockingFactory::class)
class BlockingViewModel @AssistedInject constructor(
	private val amplitude: Amplitude,
	@Assisted private val groupId: Long,
	@Assisted private val groupName: String,
	@Assisted private val groupAppCount: Int,
) : ViewModel() {

	@AssistedFactory
	interface BlockingFactory {
		fun create(
			groupId: Long,
			groupName: String,
			groupAppCount: Int,
		): BlockingViewModel
	}

	init {
		viewModelScope.launch(Dispatchers.IO) {
			trackViewCoolDown(groupId)
		}
	}

	// ============ Amplitude Event Tracking Functions ============

	/**
	 * Amplitude 이벤트 전송 공통 함수
	 */
	private fun trackAmplitudeEvent(event: AmplitudeEvent) {
		amplitude.track(event.getEventName(), event.toEventProperties())
	}

	/**
	 * 15. view_cooldown 이벤트 전송
	 * 쿨다운 화면이 나타날 때 트리거
	 */
	fun trackViewCoolDown(groupId: Long) {
		trackAmplitudeEvent(
			AmplitudeEventHelper.createViewCooldownEvent(
				groupId = groupId.toString(),
				groupName = groupName,
				groupAppCount = groupAppCount,
			),
		)
	}
}
