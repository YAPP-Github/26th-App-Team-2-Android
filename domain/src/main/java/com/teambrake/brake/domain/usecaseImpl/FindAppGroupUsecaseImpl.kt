package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.usecase.FindAppGroupUseCase
import javax.inject.Inject

class FindAppGroupUsecaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : FindAppGroupUseCase {

	override suspend operator fun invoke(packageName: String): AppGroup? {
		val groupId = appRepository.getAppGroupIdByPackage(packageName) ?: return null
		return appGroupRepository.getAppGroupById(groupId)
	}
}
