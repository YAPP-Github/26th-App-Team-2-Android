package com.yapp.breake.presentation.onboarding.guide

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.presentation.onboarding.guide.model.GuideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
) : ViewModel() {
	private val _snackBarFlow = MutableSharedFlow<UiString>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<GuideEffect>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	fun popBackStack() {
		viewModelScope.launch {
			_navigationFlow.emit(GuideEffect.NavigateToBack)
		}
	}

	fun continueFromGuide(context: Context) {
		viewModelScope.launch {
			if (permissionManager.isAllGranted(context)) {
				_navigationFlow.emit(GuideEffect.NavigateToComplete)
			} else {
				_navigationFlow.emit(GuideEffect.NavigateToPermission)
			}
		}
	}
}
