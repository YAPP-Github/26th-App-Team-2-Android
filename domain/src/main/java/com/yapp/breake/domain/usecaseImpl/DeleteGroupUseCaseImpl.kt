package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.usecase.DeleteGroupUseCase
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
