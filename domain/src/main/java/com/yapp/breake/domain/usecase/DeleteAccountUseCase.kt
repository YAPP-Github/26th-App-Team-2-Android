package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination

interface DeleteAccountUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Destination
}
