package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.data.remote.source.StatisticRemoteDataSource
import com.teambrake.brake.data.repository.util.OfflineBlocker
import com.teambrake.brake.data.repository.util.OfflineException
import com.teambrake.brake.domain.repository.StatisticRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

internal class StatisticRepositoryImpl @Inject constructor(
	private val offlineBlocker: OfflineBlocker,
	private val statisticRemoteDataSource: StatisticRemoteDataSource,
) : StatisticRepository {

	override suspend fun pushSession(appGroup: AppGroup): Result<Unit> = try {
		offlineBlocker.block {
			statisticRemoteDataSource.pushSession(
				appGroup = appGroup,
				onSuccess = {
					Result.success(Unit)
				},
			)
		}
		Result.success(Unit)
	} catch (e: OfflineException) {
		Result.failure(e)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override fun getStatistics(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?> {
		val today = LocalDate.now()
		val startDate = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
		val endDate = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))

		return offlineBlocker.blockFlow {
			statisticRemoteDataSource.getStatistic(
				startDate = startDate,
				endDate = endDate,
				onError = onError,
			)
		}.catch {
			if (it is OfflineException) {
				onError(it)
				// TODO: 오프라인의 경우 local db 통계 데이터를 가져오는 로직 추가 예정
				emit(null)
			} else {
				throw it
			}
		}
	}
}
