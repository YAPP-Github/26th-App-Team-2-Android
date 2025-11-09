package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.usecase.GrantNewGroupIdUseCase
import javax.inject.Inject

class GrantNewGroupIdUseCaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
) : GrantNewGroupIdUseCase {
	override suspend fun invoke(onError: suspend (Throwable) -> Unit): Long = appGroupRepository.getAvailableMinGroupId()
}
