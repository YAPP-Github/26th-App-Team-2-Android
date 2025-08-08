package com.yapp.breake.domain.usecase

interface GrantNewGroupIdUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Long
}
