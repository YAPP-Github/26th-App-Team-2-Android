package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import javax.inject.Inject

class FindAppGroupUsecase @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) {

	suspend operator fun invoke(packageName: String): AppGroup? {
		val groupId = appRepository.getAppGroupIdByPackage(packageName) ?: return null
		return appGroupRepository.getAppGroupById(groupId)
	}
}
