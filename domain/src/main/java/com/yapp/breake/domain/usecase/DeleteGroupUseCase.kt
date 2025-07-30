package com.yapp.breake.domain.usecase

interface DeleteGroupUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
		groupId: Long,
	)
}
