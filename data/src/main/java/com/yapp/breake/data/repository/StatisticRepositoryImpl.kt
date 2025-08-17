package com.yapp.breake.data.repository

import com.yapp.breake.core.model.app.Session
import com.yapp.breake.core.model.app.Statistics
import com.yapp.breake.data.remote.source.StatisticRemoteDataSource
import com.yapp.breake.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

internal class StatisticRepositoryImpl @Inject constructor(
	private val statisticRemoteDataSource: StatisticRemoteDataSource,
) : StatisticRepository {

	override suspend fun pushSession(session: Session): Result<Unit> {
		return try {
			statisticRemoteDataSource.pushSession(
				session = session,
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
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>> {
		return statisticRemoteDataSource.getStatistic(
			startDate = startDate,
			endDate = endDate,
			onError = onError,
		)
	}
}

