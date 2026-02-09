package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DeleteGroupUseCaseError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.usecase.DeleteGroupUseCase
import javax.inject.Inject

class DeleteGroupUseCaseImpl @Inject constructor(
	private val appRepository: AppRepository,
	private val appGroupRepository: AppGroupRepository,
) : DeleteGroupUseCase {

	override suspend fun invoke(groupId: Long): BrakeResult<Unit, DeleteGroupUseCaseError> {
		val groupResult = appGroupRepository.deleteAppGroupByGroupId(groupId)

		if (groupResult.isFailure) {
			val e = groupResult.exceptionOrNull()
			return BrakeResult.Error(LocalApiCallError(e ?: Exception("앱 그룹 삭제 실패")))
		}

		val appResult = appRepository.deleteAppByParentGroupId(groupId)

		return appResult.fold(
			onSuccess = {
				BrakeResult.Success(Unit)
			},
			onFailure = { e ->
				BrakeResult.Error(LocalApiCallError(e))
			},
		)
	}
}
