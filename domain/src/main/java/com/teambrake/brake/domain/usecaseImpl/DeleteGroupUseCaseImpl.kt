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

		when {
			groupResult.isSuccess -> {
				// 성공적으로 앱 그룹 삭제됨
			}
			groupResult.isFailure -> {
				val exception = groupResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 그룹 삭제 실패")))
			}
		}

		val appResult = appRepository.deleteAppByParentGroupId(groupId)

		return when {
			appResult.isSuccess -> {
				BrakeResult.Success(Unit)
			}
			appResult.isFailure -> {
				val exception = appResult.exceptionOrNull()
				BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 삭제 실패")))
			}
			else -> BrakeResult.Error(LocalApiCallError(Exception("알 수 없는 오류")))
		}
	}
}
