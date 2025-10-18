package com.teambrake.brake.domain.repository

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import kotlinx.coroutines.flow.Flow

interface StatisticRepository {

	suspend fun pushSession(appGroup: AppGroup): Result<Unit>

	fun getStatistics(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?>
}
