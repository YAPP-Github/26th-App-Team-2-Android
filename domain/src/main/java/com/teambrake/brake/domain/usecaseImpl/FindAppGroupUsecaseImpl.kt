package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.usecase.FindAppGroupUseCase
import javax.inject.Inject

class FindAppGroupUsecaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : FindAppGroupUseCase {

	override suspend operator fun invoke(packageName: String): BrakeResult<AppGroup?, UndefinedExceptionError> {
		val groupId = appRepository.getAppGroupIdByPackage(packageName)
		if (groupId == null) {
			return BrakeResult.Success(null)
		}
		val appGroup = appGroupRepository.getAppGroupById(groupId)
		return BrakeResult.Success(appGroup)
	}
}
