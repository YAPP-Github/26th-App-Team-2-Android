package com.yapp.breake.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.SetBlockingAlarmUseCase
import com.yapp.breake.presentation.home.contract.HomeEvent
import com.yapp.breake.presentation.home.contract.HomeModalState
import com.yapp.breake.presentation.home.contract.HomeUiState
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
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

	val homeUiState: StateFlow<HomeUiState> = appGroupRepository
		.observeAppGroup()
		.map(::returnHomeUiState)
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.Eagerly,
			initialValue = HomeUiState.Loading,
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
		firebaseAnalytics.logEvent("try_stop_session") {
			param("where", "home_screen")
		}
	}

	fun stopAppUsing(appGroup: AppGroup) {
		viewModelScope.launch {
			setBlockingAlarmUseCase(
				groupId = appGroup.id,
			).onSuccess {
				showStopUsingSuccess(appGroup.name)
			}
		}
	}

	private fun showStopUsingSuccess(groupName: String) {
		viewModelScope.launch {
			_homeEvent.emit(HomeEvent.ShowStopUsingSuccess(groupName))
		}
		firebaseAnalytics.logEvent("stop_session") {
			param("reason", "user_stop")
		}
	}

	fun navigateToRegistry(groupId: Long? = null) {
		viewModelScope.launch {
			_homeEvent.emit(HomeEvent.NavigateToRegistry(groupId))
		}
		if (groupId != null) {
			firebaseAnalytics.logEvent("try_edit_existed_group") {
				param("group_id", groupId)
			}
		} else {
			firebaseAnalytics.logEvent("try_add_new_group") {
				param("group_id", "null")
			}
		}
	}

	fun dismiss() {
		_homeModalState.update { HomeModalState.Nothing }
	}
}
