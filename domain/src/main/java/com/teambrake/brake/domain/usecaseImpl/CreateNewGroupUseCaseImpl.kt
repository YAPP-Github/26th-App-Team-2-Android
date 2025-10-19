package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.usecase.CreateNewGroupUseCase
import javax.inject.Inject

class CreateNewGroupUseCaseImpl @Inject constructor(
	private val appRepository: AppRepository,
	private val appGroupRepository: AppGroupRepository,
) : CreateNewGroupUseCase {

	override suspend fun invoke(
		onError: suspend (Throwable) -> Unit,
		group: AppGroup,
	) {
		try {
			val appGroup = appGroupRepository.insertAppGroup(group)

			appRepository.insertApps(appGroup.id, appGroup.apps)
		} catch (e: Exception) {
			onError(e)
		}
	}
}
