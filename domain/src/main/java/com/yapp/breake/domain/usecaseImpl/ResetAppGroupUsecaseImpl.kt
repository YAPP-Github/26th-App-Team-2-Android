package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.ResetAppGroupUsecase
import javax.inject.Inject

class ResetAppGroupUsecaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
) : ResetAppGroupUsecase {

	override suspend fun invoke(appGroup: AppGroup): Result<Unit> {
		appGroupRepository.updateGroupSessionInfo(
			groupId = appGroup.id,
			goalMinutes = null,
			sessionStartTime = null,
		)

		return appGroupRepository.updateAppGroupState(
			groupId = appGroup.id,
			appGroupState = AppGroupState.NeedSetting,
		).also {

			appGroupRepository.resetSnooze(appGroup.id)
		}
	}
}
