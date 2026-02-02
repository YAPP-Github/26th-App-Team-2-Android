package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.StatisticRemoteDataSource
import com.teambrake.brake.data.repository.base.BaseRepository
import com.teambrake.brake.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

internal class StatisticRepositoryImpl @Inject constructor(
	tokenLocalDataSource: TokenLocalDataSource,
	private val statisticRemoteDataSource: StatisticRemoteDataSource,
) : BaseRepository(tokenLocalDataSource),
	StatisticRepository {

	override suspend fun pushSession(appGroup: AppGroup): Result<Unit> = try {
		executeWithStatusCheck(
			runIfOnline = {
				val result = statisticRemoteDataSource.pushSession(appGroup = appGroup)
				if (result.isSuccess) {
					Result.success(Unit)
				} else {
					Result.failure(result.exceptionOrNull() ?: Exception("세션 전송에 실패했습니다"))
				}
			},
			runIfOffline = {
				Result.success(Unit)
			},
		)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override fun getStatistics(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?> {
		val today = LocalDate.now()
		val startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
		val endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

		return executeFlowWithStatusCheck(
			flowProvider = {
				statisticRemoteDataSource.getStatistic(
					startDate = startDate,
					endDate = endDate,
					onError = onError,
				)
			},
			offlineFlowProvider = {
				flow {
					// TODO: 오프라인의 경우 local db 통계 데이터를 가져오는 로직 추가 예정
					emit(null)
				}
			},
		)
	}
}
