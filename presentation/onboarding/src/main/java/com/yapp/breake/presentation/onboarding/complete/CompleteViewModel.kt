package com.yapp.breake.presentation.onboarding.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCase
import com.yapp.breake.presentation.onboarding.R
import com.yapp.breake.presentation.onboarding.complete.model.CompleteEffect
import com.yapp.breake.presentation.onboarding.guide.model.GuideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteViewModel @Inject constructor(
	private val storeOnboardingCompletionUseCase: StoreOnboardingCompletionUseCase,
) : ViewModel() {
	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<GuideEffect>()
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
			_navigationFlow.emit(CompleteEffect.NavigateToMain)
		}
	}
}
