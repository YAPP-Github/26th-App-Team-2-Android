package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.ResetAppGroupUseCaseError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.StatisticRepository
import com.teambrake.brake.domain.usecase.ResetAppGroupUsecase
import javax.inject.Inject

class ResetAppGroupUsecaseImpl @Inject constructor(
	private val appGroupRepository: AppGroupRepository,
	private val statisticRepository: StatisticRepository,
) : ResetAppGroupUsecase {

	override suspend fun invoke(appGroup: AppGroup): BrakeResult<Unit, ResetAppGroupUseCaseError> {
		statisticRepository.pushSession(appGroup).onFailure { e ->
			return BrakeResult.Error(LocalApiCallError(e))
		}

		val sessionResult = appGroupRepository.updateGroupSessionInfo(
			groupId = appGroup.id,
			goalMinutes = null,
			sessionStartTime = null,
		)

		when {
			sessionResult.isSuccess -> {
				// 성공적으로 업데이트됨
			}

			sessionResult.isFailure -> {
				val exception = sessionResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("그룹 세션 정보 업데이트 실패")))
			}
		}

		val stateResult = appGroupRepository.updateAppGroupState(
			groupId = appGroup.id,
			appGroupState = AppGroupState.NeedSetting,
		)

		appGroupRepository.resetSnooze(appGroup.id)

		return when {
			stateResult.isSuccess -> {
				BrakeResult.Success(Unit)
			}

			stateResult.isFailure -> {
				val exception = stateResult.exceptionOrNull()
				BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 그룹 상태 업데이트 실패")))
			}

			else -> BrakeResult.Error(LocalApiCallError(Exception("예상치 못한 오류")))
		}
	}
}
