package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.app.Session
import com.yapp.breake.core.model.app.Statistics
import com.yapp.breake.data.mapper.toDateString
import com.yapp.breake.data.mapper.toSessionRequest
import com.yapp.breake.data.mapper.toStatistics
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

internal class StatisticRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : StatisticRemoteDataSource {

	override suspend fun pushSession(
		session: Session,
		onSuccess: suspend (Long) -> Unit,
		onError: suspend (Throwable) -> Unit,
	) {
		retrofitBrakeApi.sendSession(session.toSessionRequest())
			.suspendOnSuccess {
				onSuccess(data.data.sessionId)
			}.suspendOnFailure {
				onError(Throwable("세션을 생성하는 중 오류가 발생했습니다"))
			}
	}

	override fun getStatistic(
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>> = flow {
		retrofitBrakeApi.getStatistics(
			start = startDate.toDateString(),
			end = endDate.toDateString(),
		).suspendOnSuccess {
			emit(data.data.toStatistics())
		}.suspendOnFailure {
			onError(Throwable("통계 정보를 가져오는 중 오류가 발생했습니다"))
		}
	}
}
