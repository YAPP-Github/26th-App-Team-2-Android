package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.usecase.CreateNewGroupUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CreateNewGroupUseCaseImpl @Inject constructor(
	private val appRepository: AppRepository,
	private val appGroupRepository: AppGroupRepository,
) : CreateNewGroupUseCase {

	override suspend fun invoke(
		onError: suspend (Throwable) -> Unit,
		group: AppGroup,
	) {
		withContext(Dispatchers.IO) {
			try {
				appGroupRepository.insertAppGroup(group)
				group.apps.forEach { app ->
					appRepository.insertApp(group.id, app)
				}
			} catch (e: Exception) {
				onError(e)
			}
		}
	}
}
