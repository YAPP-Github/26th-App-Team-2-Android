package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.CreateNewGroupUseCaseError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.usecase.CreateNewGroupUseCase
import javax.inject.Inject

class CreateNewGroupUseCaseImpl @Inject constructor(
	private val appRepository: AppRepository,
	private val appGroupRepository: AppGroupRepository,
) : CreateNewGroupUseCase {

	override suspend fun invoke(group: AppGroup): BrakeResult<Unit, CreateNewGroupUseCaseError> {
		val appGroup = appGroupRepository.insertAppGroup(group)

		val result = appRepository.insertApps(appGroup.id, appGroup.apps)

		return when {
			result.isSuccess -> {
				BrakeResult.Success(Unit)
			}
			result.isFailure -> {
				val exception = result.exceptionOrNull()
				BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 삽입 실패")))
			}
			else -> BrakeResult.Error(LocalApiCallError(Exception("알 수 없는 오류")))
		}
	}
}
