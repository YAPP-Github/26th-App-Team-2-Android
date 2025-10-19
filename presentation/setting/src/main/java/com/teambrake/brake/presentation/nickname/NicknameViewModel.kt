package com.teambrake.brake.presentation.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.ui.SnackBarState
import com.teambrake.brake.core.ui.UiString
import com.teambrake.brake.domain.usecase.GetNicknameUseCase
import com.teambrake.brake.domain.usecase.UpdateNicknameUseCase
import com.teambrake.brake.presentation.nickname.model.NicknameNavState
import com.teambrake.brake.presentation.nickname.model.NicknameUiState
import com.teambrake.brake.presentation.setting.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
	getNicknameUseCase: GetNicknameUseCase,
	private val updateNicknameUseCase: UpdateNicknameUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

	private var updateJob: Job? = null

	private val _nicknameUiState: MutableStateFlow<NicknameUiState> =
		MutableStateFlow(NicknameUiState.NicknameIdle(nickname = ""))
	val nicknameUiState = _nicknameUiState.asStateFlow()

	private val _snackBarFlow = MutableSharedFlow<SnackBarState>()
	val snackBarFlow = _snackBarFlow.asSharedFlow()

	private val _navigationFlow = MutableSharedFlow<NicknameNavState>()
	val navigationFlow = _navigationFlow.asSharedFlow()

	init {
		viewModelScope.launch {
			val nickname = getNicknameUseCase(
				onError = { /* 에러 핸들링 스킵*/ },
			).first()
			_nicknameUiState.value = NicknameUiState.NicknameIdle(nickname = nickname)
		}
	}

	fun typeNickname(nickname: String) {
		_nicknameUiState.value = NicknameUiState.NicknameIdle(nickname = nickname)
	}

	fun updateNickname() {
		val nickname = _nicknameUiState.value.nickname
		_nicknameUiState.value = NicknameUiState.NicknameUpdating(
			nickname = nickname,
		)
		updateJob = viewModelScope.launch {
			updateNicknameUseCase(
				nickname = nickname,
				onError = {
					_snackBarFlow.emit(
						SnackBarState.Error(
							uiString = UiString.ResourceString(R.string.nickname_snackbar_update_error),
						),
					)
				},
				onSuccess = {
					_snackBarFlow.emit(
						SnackBarState.Success(
							uiString = UiString.ResourceString(R.string.nickname_snackbar_update_success),
						),
					)
					firebaseAnalytics.logEvent("update_nickname") {
						param("nickname", nickname)
					}
					_navigationFlow.emit(NicknameNavState.NavigateToSetting)
				},
			)
		}
	}

	fun cancelUpdateNickname() {
		updateJob?.run {
			cancel()
			_nicknameUiState.value = NicknameUiState.NicknameIdle(
				nickname = _nicknameUiState.value.nickname,
			)
		}
	}

	fun popBackStack() {
		viewModelScope.launch {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "setting_screen")
			}
			_navigationFlow.emit(NicknameNavState.PopBackStack)
		}
	}
}
