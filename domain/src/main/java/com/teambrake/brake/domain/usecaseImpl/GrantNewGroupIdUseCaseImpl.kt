package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.GrantNewGroupIdUseCaseError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.usecase.GrantNewGroupIdUseCase
import javax.inject.Inject

class GrantNewGroupIdUseCaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
) : GrantNewGroupIdUseCase {
	override suspend fun invoke(): BrakeResult<Long, GrantNewGroupIdUseCaseError> {
		val groupId = appGroupRepository.getAvailableMinGroupId()
		return BrakeResult.Success(groupId)
	}
}
