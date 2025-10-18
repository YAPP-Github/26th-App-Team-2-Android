package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.data.remote.source.StatisticRemoteDataSource
import com.teambrake.brake.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

internal class StatisticRepositoryImpl @Inject constructor(
	private val statisticRemoteDataSource: StatisticRemoteDataSource,
) : StatisticRepository {

	override suspend fun pushSession(appGroup: AppGroup): Result<Unit> {
		return try {
			statisticRemoteDataSource.pushSession(
				appGroup = appGroup,
				onSuccess = {
					Result.success(Unit)
				},
			)

			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override fun getStatistics(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?> {
		val today = LocalDate.now()
		val startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
		val endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

		return statisticRemoteDataSource.getStatistic(
			startDate = startDate,
			endDate = endDate,
			onError = onError,
		)
	}
}
