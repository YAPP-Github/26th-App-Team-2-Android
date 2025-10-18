package com.teambrake.brake.data.remote.source

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

internal interface StatisticRemoteDataSource {

	suspend fun pushSession(
		appGroup: AppGroup,
		onSuccess: suspend (Long) -> Unit,
		onError: suspend (Throwable) -> Unit = {},
	)

	fun getStatistic(
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?>
}
