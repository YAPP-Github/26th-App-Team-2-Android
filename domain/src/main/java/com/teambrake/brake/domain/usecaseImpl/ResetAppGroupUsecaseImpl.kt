package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.StatisticRepository
import com.teambrake.brake.domain.usecase.ResetAppGroupUsecase
import javax.inject.Inject

class ResetAppGroupUsecaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
	private val statisticRepository: StatisticRepository,
) : ResetAppGroupUsecase {

	override suspend fun invoke(appGroup: AppGroup): Result<Unit> {
		statisticRepository.pushSession(appGroup)

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
