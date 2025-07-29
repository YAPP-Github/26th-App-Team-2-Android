package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AppGroupRepository
import javax.inject.Inject

class ResetAppGroupUsecaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
) : ResetAppGroupUsecase {

	override suspend fun invoke(groupId: Long): Result<Unit> {
		return appGroupRepository.updateAppGroupState(
			groupId = groupId,
			appGroupState = AppGroupState.NeedSetting,
			endTime = null,
		).also {
			appGroupRepository.resetSnooze(groupId)
		}
	}
}
