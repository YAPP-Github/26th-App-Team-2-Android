package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.data.remote.model.AppGroupRequest
import com.yapp.breake.data.remote.model.AppGroupResponse
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class AppGroupRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : AppGroupRemoteDataSource {

	override fun createAppGroup(
		request: AppGroupRequest,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroupResponse> = flow {
		retrofitBrakeApi.createAppGroup(request)
			.suspendOnSuccess {
				emit(this.data)
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 생성하는 중 오류가 발생했습니다"))
			}
	}

	override fun updateAppGroup(
		groupId: Long,
		request: AppGroupRequest,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroupResponse> = flow {
		retrofitBrakeApi.updateAppGroup(groupId, request)
			.suspendOnSuccess {
				emit(this.data)
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 수정하는 중 오류가 발생했습니다"))
			}
	}

	override fun deleteAppGroup(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	): Flow<Unit> = flow {
		retrofitBrakeApi.deleteAppGroup(groupId)
			.suspendOnSuccess {
				emit(Unit)
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 삭제하는 중 오류가 발생했습니다"))
			}
	}
}
