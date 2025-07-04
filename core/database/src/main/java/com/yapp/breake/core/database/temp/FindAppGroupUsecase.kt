package com.yapp.breake.core.database.temp

import com.yapp.breake.core.model.app.AppGroup
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
