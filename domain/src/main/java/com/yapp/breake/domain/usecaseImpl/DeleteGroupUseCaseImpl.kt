package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.usecase.DeleteGroupUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
			withContext(Dispatchers.IO) {
				appGroupRepository.deleteAppGroupByGroupId(groupId)
				appRepository.deleteAppByParentGroupId(groupId)
			}
		} catch (e: Exception) {
			onError(e)
		}
	}
}
