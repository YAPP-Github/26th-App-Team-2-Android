package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticRepository {

	suspend fun pushSession(appGroup: AppGroup): Result<Unit>

	fun getStatistics(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?>
}
