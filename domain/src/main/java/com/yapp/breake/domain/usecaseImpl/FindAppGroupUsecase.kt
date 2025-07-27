package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.usecase.FindAppGroupUseCase
import javax.inject.Inject

class FindAppGroupUsecase @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : FindAppGroupUseCase {

	override suspend operator fun invoke(packageName: String): AppGroup? {
		val groupId = appRepository.getAppGroupIdByPackage(packageName) ?: return null
		return appGroupRepository.getAppGroupById(groupId)
	}
}
