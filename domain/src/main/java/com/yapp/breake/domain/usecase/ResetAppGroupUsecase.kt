package com.yapp.breake.domain.usecase

interface ResetAppGroupUsecase {
	suspend operator fun invoke(
		groupId: Long,
	): Result<Unit>
}
