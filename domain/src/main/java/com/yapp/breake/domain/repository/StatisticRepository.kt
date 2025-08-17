package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.Session
import com.yapp.breake.core.model.app.Statistics
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatisticRepository {

	suspend fun pushSession(session: Session): Result<Unit>

	fun getStatistics(
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>>
}

