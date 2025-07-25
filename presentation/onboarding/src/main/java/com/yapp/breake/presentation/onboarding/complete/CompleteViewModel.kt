package com.yapp.breake.presentation.onboarding.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCase
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
	private val _errorFlow = MutableSharedFlow<Throwable>()
	val errorFlow = _errorFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<GuideEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun completeOnboarding() {
		viewModelScope.launch {
			storeOnboardingCompletionUseCase(
				isComplete = true,
				onError = { error ->
					_errorFlow.tryEmit(error)
				},
			)
			_navigationFlow.emit(CompleteEffect.NavigateToMain)
		}
	}
}
