package com.teambrake.brake.presentation.onboarding.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amplitude.android.Amplitude
import com.amplitude.core.events.Identify
import com.teambrake.brake.presentation.onboarding.complete.model.CompleteNavState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.amplitude.StepDetail
import com.teambrake.brake.core.amplitude.StepName
import com.teambrake.brake.core.ui.UiString
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.usecase.StoreOnboardingCompletionUseCase
import com.teambrake.brake.presentation.onboarding.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
	private val storeOnboardingCompletionUseCase: StoreOnboardingCompletionUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
	private val amplitude: Amplitude,
) : ViewModel() {
	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<CompleteNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	init {
		// 온보딩 완료 화면(welcome) 진입 시 이벤트 트래킹
		trackWelcomeView()
	}

	/**
	 * view_onboarding 이벤트 전송 (welcome 화면)
	 */
	private fun trackWelcomeView() {
		val event = AmplitudeEventHelper.createViewOnboardingEvent(
			stepName = StepName.WELCOME,
			stepDetail = StepDetail.DEFAULT,
		)
		amplitude.track(event.getEventName(), event.toEventProperties())
	}

	fun completeOnboarding() {
		viewModelScope.launch {
			val result = storeOnboardingCompletionUseCase(
				isComplete = true,
			)
			when (result) {
				is BrakeResult.Success -> {
					launch(Dispatchers.IO) {
						// 3. complete_onboarding 이벤트 전송
						val completeEvent = AmplitudeEventHelper.createCompleteOnboardingEvent()
						amplitude.track(completeEvent.getEventName(), completeEvent.toEventProperties())

						// 18. is_onboarding_completed User Property 업데이트
						val userProperty = AmplitudeEventHelper.setIsOnboardingCompleted(isCompleted = true)
						amplitude.identify(
							Identify().apply {
								userProperty.toUserProperties().forEach { (key, value) ->
									set(key, value)
								}
							},
						)

						firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE) {
							param(FirebaseAnalytics.Param.SUCCESS, "true")
						}
					}
					_navigationFlow.emit(CompleteNavState.NavigateToMain)
				}
				is BrakeResult.Error -> {
					_snackBarFlow.emit(
						UiString.ResourceString(R.string.onboarding_snackbar_flag_save_error),
					)
				}
			}
		}
	}
}
