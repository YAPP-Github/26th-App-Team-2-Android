package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.usecase.DeleteGroupUseCase
import javax.inject.Inject

class DeleteGroupUseCaseImpl @Inject constructor(
	private val appRepository: AppRepository,
	private val appGroupRepository: AppGroupRepository,
) : DeleteGroupUseCase {

	override suspend fun invoke(
		onError: suspend (Throwable) -> Unit,
		groupId: Long,
	) {
		try {
			appGroupRepository.deleteAppGroupByGroupId(groupId)
			appRepository.deleteAppByParentGroupId(groupId)
		} catch (e: Exception) {
			onError(e)
		}
	}
}
