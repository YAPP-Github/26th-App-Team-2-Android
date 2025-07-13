package com.yapp.breake.presentation.overlay

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.yapp.breake.core.common.IntentConstants
import com.yapp.breake.core.model.app.BlockingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OverlayViewModel @Inject constructor(
	private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

	val packageName: StateFlow<String> get() = savedStateHandle.getStateFlow(
		IntentConstants.EXTRA_PACKAGE_NAME,
		"",
	)
	val blockingState: StateFlow<BlockingState> get() = savedStateHandle.getStateFlow(
		IntentConstants.EXTRA_BLOCKING_STATE,
		BlockingState.NEEDS_SETTING,
	)
}
