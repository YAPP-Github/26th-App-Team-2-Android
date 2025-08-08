package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.GrantNewGroupIdUseCase
import javax.inject.Inject

class GrantNewGroupIdUseCaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
) : GrantNewGroupIdUseCase {
	override suspend fun invoke(onError: suspend (Throwable) -> Unit): Long {
		return appGroupRepository.getAvailableMinGroupId()
	}
}
