package com.yapp.breake.presentation.nickname.model

import androidx.compose.runtime.Stable

@Stable
sealed interface NicknameUiState {

	val nickname: String

	@Stable
	data class NicknameIdle(override val nickname: String) : NicknameUiState

	@Stable
	data class NicknameUpdating(override val nickname: String) : NicknameUiState
}
