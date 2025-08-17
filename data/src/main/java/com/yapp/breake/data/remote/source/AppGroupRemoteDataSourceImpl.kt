package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.data.mapper.toAppGroup
import com.yapp.breake.data.mapper.toAppGroupRequest
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class AppGroupRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : AppGroupRemoteDataSource {

	override fun createAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroup> = flow {
		retrofitBrakeApi.createAppGroup(appGroup.toAppGroupRequest())
			.suspendOnSuccess {
				emit(data.toAppGroup())
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 생성하는 중 오류가 발생했습니다"))
			}
	}

	override fun updateAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroup> = flow {
		retrofitBrakeApi.updateAppGroup(appGroup.id, appGroup.toAppGroupRequest())
			.suspendOnSuccess {
				emit(data.toAppGroup())
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 수정하는 중 오류가 발생했습니다"))
			}
	}

	override suspend fun deleteAppGroup(
		groupId: Long,
		onSuccess: suspend () -> Unit,
		onError: suspend (Throwable) -> Unit,
	) {
		retrofitBrakeApi.deleteAppGroup(groupId)
			.suspendOnSuccess {
				onSuccess()
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 삭제하는 중 오류가 발생했습니다"))
			}
	}
}
