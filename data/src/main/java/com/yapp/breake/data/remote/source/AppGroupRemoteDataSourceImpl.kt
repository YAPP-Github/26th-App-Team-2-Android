package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.data.mapper.toAppGroupRequest
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import javax.inject.Inject

internal class AppGroupRemoteDataSourceImpl @Inject constructor(
    private val retrofitBrakeApi: RetrofitBrakeApi,
) : AppGroupRemoteDataSource {

    override suspend fun createAppGroup(
        appGroup: AppGroup,
        onSuccess: suspend () -> Unit,
        onError: suspend (Throwable) -> Unit,
    ) {
        retrofitBrakeApi.createAppGroup(appGroup.toAppGroupRequest())
            .suspendOnSuccess {
                onSuccess()
            }.suspendOnFailure {
                onError(Throwable("앱 그룹을 생성하는 중 오류가 발생했습니다"))
            }
    }

    override suspend fun updateAppGroup(
        appGroup: AppGroup,
        onSuccess: suspend () -> Unit,
        onError: suspend (Throwable) -> Unit,
    ) {
        retrofitBrakeApi.updateAppGroup(appGroup.id, appGroup.toAppGroupRequest())
            .suspendOnSuccess {
                onSuccess()
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
