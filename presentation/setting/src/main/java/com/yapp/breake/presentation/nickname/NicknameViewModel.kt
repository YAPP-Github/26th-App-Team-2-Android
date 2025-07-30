package com.yapp.breake.presentation.nickname

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yapp.breake.core.ui.UiString
import com.yapp.breake.domain.usecase.GetNicknameUseCase
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import com.yapp.breake.presentation.nickname.model.NicknameNavState
import com.yapp.breake.presentation.nickname.model.NicknameSnackBarState
import com.yapp.breake.presentation.nickname.model.NicknameUiState
import com.yapp.breake.presentation.setting.R
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
) : ViewModel() {

	private var updateJob: Job? = null

	private val _nicknameUiState: MutableStateFlow<NicknameUiState> =
		MutableStateFlow(NicknameUiState.NicknameIdle(nickname = ""))
	val nicknameUiState = _nicknameUiState.asStateFlow()

	private val _snackBarFlow = MutableSharedFlow<NicknameSnackBarState>()
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
						NicknameSnackBarState.Error(
							uiString = UiString.ResourceString(R.string.nickname_snackbar_update_error),
						),
					)
				},
				onSuccess = {
					_snackBarFlow.emit(
						NicknameSnackBarState.Success(
							uiString = UiString.ResourceString(R.string.nickname_snackbar_update_success),
						),
					)
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
}
