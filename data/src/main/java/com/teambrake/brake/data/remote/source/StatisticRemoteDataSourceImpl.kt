package com.teambrake.brake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.data.mapper.toDateString
import com.teambrake.brake.data.mapper.toSessionRequest
import com.teambrake.brake.data.mapper.toStatistics
import com.teambrake.brake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

internal class StatisticRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : StatisticRemoteDataSource {

	override suspend fun pushSession(appGroup: AppGroup): Result<Long> {
		val request = appGroup.toSessionRequest() ?: run {
			return Result.failure(Throwable("세션 요청을 생성하는 중 오류가 발생했습니다"))
		}

		var result: Result<Long> = Result.failure(Throwable("알 수 없는 오류"))
		retrofitBrakeApi.sendSession(request)
			.suspendOnSuccess {
				result = Result.success(data.data.sessionId)
			}.suspendOnFailure {
				result = Result.failure(Throwable("세션 정보를 전송하는 중 오류가 발생했습니다"))
			}
		return result
	}

	override fun getStatistic(
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?> = flow {
		retrofitBrakeApi.getStatistics(
			start = startDate.toDateString(),
			end = endDate.toDateString(),
		).suspendOnSuccess {
			emit(data.data.toStatistics())
		}.suspendOnFailure {
			emit(null)
			onError(Throwable("통계 정보를 가져오는 중 오류가 발생했습니다"))
		}
	}
}
