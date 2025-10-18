package com.teambrake.brake.domain.usecase

interface DeleteGroupUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
		groupId: Long,
	)
}
