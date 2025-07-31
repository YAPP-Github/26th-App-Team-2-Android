package com.yapp.breake.presentation.home

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.SetBlockingAlarmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
	appGroupRepository: AppGroupRepository,
	private val setBlockingAlarmUseCase: SetBlockingAlarmUseCase,
) : ViewModel() {

	val homeUiState: StateFlow<HomeUiState> = appGroupRepository
		.observeAppGroup()
		.map(::returnHomeUiState)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = HomeUiState.Nothing,
		)

	private val _homeModalState: MutableStateFlow<HomeModalState> =
		MutableStateFlow(HomeModalState.Nothing)
	val homeModalState: StateFlow<HomeModalState> get() = _homeModalState

	private val _homeEvent: MutableSharedFlow<HomeEvent> = MutableSharedFlow()
	val homeEvent: MutableSharedFlow<HomeEvent> get() = _homeEvent

	private fun returnHomeUiState(appGroups: List<AppGroup>): HomeUiState {
		if (appGroups.isEmpty()) {
			return HomeUiState.Nothing
		}

		appGroups.forEach { appGroup ->
			when (appGroup.appGroupState) {
				AppGroupState.Using -> return HomeUiState.Using(appGroup)
				AppGroupState.Blocking, AppGroupState.SnoozeBlocking -> return HomeUiState.Blocking(
					appGroup,
				)

				AppGroupState.NeedSetting -> {}
			}
		}

		return HomeUiState.GroupList(appGroups)
	}

	fun showStopUsingDialog(appGroup: AppGroup) {
		_homeModalState.update {
			HomeModalState.StopUsingDialog(appGroup)
		}
	}

	fun stopAppUsing(appGroup: AppGroup) {
		viewModelScope.launch {
			setBlockingAlarmUseCase(
				groupId = appGroup.id,
				appName = "",
			).onSuccess {
				showStopUsingSuccess(appGroup.name)
			}
		}
	}

	private fun showStopUsingSuccess(groupName: String) {
		viewModelScope.launch {
			_homeEvent.emit(HomeEvent.ShowStopUsingSuccess(groupName))
		}
	}

	fun navigateToRegistry(groupId: Long? = null) {
		viewModelScope.launch {
			_homeEvent.emit(HomeEvent.NavigateToRegistry(groupId))
		}
	}

	fun dismiss() {
		_homeModalState.update { HomeModalState.Nothing }
	}
}

@Stable
internal sealed interface HomeUiState {
	data object Nothing : HomeUiState
	data class GroupList(val appGroups: List<AppGroup>) : HomeUiState
	data class Blocking(val appGroup: AppGroup) : HomeUiState
	data class Using(val appGroup: AppGroup) : HomeUiState
}

@Stable
internal sealed interface HomeModalState {
	data object Nothing : HomeModalState
	data class StopUsingDialog(val appGroup: AppGroup) : HomeModalState
}

@Stable
internal sealed interface HomeEvent {
	data class ShowStopUsingSuccess(val groupName: String) : HomeEvent
	data class NavigateToRegistry(val groupId: Long?) : HomeEvent
}
