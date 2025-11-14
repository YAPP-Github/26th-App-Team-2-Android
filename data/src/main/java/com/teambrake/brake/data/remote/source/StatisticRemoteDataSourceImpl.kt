package com.teambrake.brake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.data.mapper.toDateString
import com.teambrake.brake.data.mapper.toSessionRequest
import com.teambrake.brake.data.mapper.toStatistics
import com.teambrake.brake.data.remote.retrofit.RetrofitBrakeApi
import com.teambrake.brake.data.remote.source.util.OfflineBlocker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

internal class StatisticRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
	private val offlineBlocker: OfflineBlocker,
) : StatisticRemoteDataSource {

	override suspend fun pushSession(
		appGroup: AppGroup,
		onSuccess: suspend (Long) -> Unit,
		onError: suspend (Throwable) -> Unit,
	) {
		offlineBlocker {
			val request = appGroup.toSessionRequest() ?: run {
				onError(Throwable("세션 요청을 생성하는 중 오류가 발생했습니다"))
				return@offlineBlocker
			}

			retrofitBrakeApi.sendSession(request)
				.suspendOnSuccess {
					onSuccess(data.data.sessionId)
				}.suspendOnFailure {
					onError(Throwable("세션을 생성하는 중 오류가 발생했습니다"))
				}
		}
	}

	override fun getStatistic(
		startDate: LocalDate,
		endDate: LocalDate,
		onError: suspend (Throwable) -> Unit,
	): Flow<List<Statistics>?> = flow {
		offlineBlocker {
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
}
