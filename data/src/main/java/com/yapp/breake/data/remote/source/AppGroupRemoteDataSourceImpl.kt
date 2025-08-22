package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.data.mapper.toAppGroup
import com.yapp.breake.data.mapper.toAppGroupRequest
import com.yapp.breake.data.remote.model.AppGroupData
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class AppGroupRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : AppGroupRemoteDataSource {

	override fun getAppGroups(onError: suspend (Throwable) -> Unit): Flow<List<AppGroup>> = flow {
		retrofitBrakeApi.getAppGroups()
			.suspendOnSuccess {
				emit(data.data.groups.map(AppGroupData::toAppGroup))
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 가져오는 중 오류가 발생했습니다"))
				Timber.e("Error fetching app groups: $this")
			}
	}

	override fun createAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroup> = flow {
		retrofitBrakeApi.createAppGroup(appGroup.toAppGroupRequest())
			.suspendOnSuccess {
				emit(data.data.toAppGroup())
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 생성하는 중 오류가 발생했습니다"))
				Timber.e("Error creating app group: $this")
			}
	}

	override fun updateAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroup> = flow {
		retrofitBrakeApi.updateAppGroup(appGroup.id, appGroup.toAppGroupRequest())
			.suspendOnSuccess {
				emit(data.data.toAppGroup())
			}.suspendOnFailure {
				onError(Throwable("앱 그룹을 수정하는 중 오류가 발생했습니다"))
				Timber.e("Error updating app group: $this")
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
				Timber.e("Error deleting app group: $this")
			}
	}
}
