package com.yapp.breake.data.remote.source

import com.yapp.breake.core.model.app.Session
import com.yapp.breake.core.model.app.Statistics
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

internal interface StatisticRemoteDataSource {

	suspend fun pushSession(
		session: Session,
		onSuccess: suspend (Long) -> Unit,
		onError: suspend (Throwable) -> Unit = {},
	)

	fun getStatistic(
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>>
}

