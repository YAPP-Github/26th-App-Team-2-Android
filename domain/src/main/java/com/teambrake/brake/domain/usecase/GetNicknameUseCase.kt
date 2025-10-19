package com.teambrake.brake.domain.usecase

import kotlinx.coroutines.flow.Flow

interface GetNicknameUseCase {
	/**
	 * Retrieves the nickname of the user.
	 *
	 * @return The user's nickname as a String.
	 */
	operator fun invoke(onError: suspend (Throwable) -> Unit): Flow<String>
}
