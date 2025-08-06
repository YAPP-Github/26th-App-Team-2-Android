package com.yapp.breake.presentation.onboarding.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCase
import com.yapp.breake.presentation.onboarding.R
import com.yapp.breake.presentation.onboarding.complete.model.CompleteNavState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
	private val storeOnboardingCompletionUseCase: StoreOnboardingCompletionUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {
	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<CompleteNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun completeOnboarding() {
		viewModelScope.launch {
			storeOnboardingCompletionUseCase(
				isComplete = true,
				onError = {
					_snackBarFlow.emit(
						UiString.ResourceString(R.string.onboarding_snackbar_flag_save_error),
					)
				},
			)
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.TUTORIAL_COMPLETE) {
				param(FirebaseAnalytics.Param.SUCCESS, "true")
			}
			_navigationFlow.emit(CompleteNavState.NavigateToMain)
		}
	}
}
