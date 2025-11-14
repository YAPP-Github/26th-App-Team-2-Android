package com.teambrake.brake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.data.mapper.toAppGroup
import com.teambrake.brake.data.mapper.toAppGroupRequest
import com.teambrake.brake.data.remote.model.AppGroupData
import com.teambrake.brake.data.remote.retrofit.RetrofitBrakeApi
import com.teambrake.brake.data.remote.source.util.OfflineBlocker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class AppGroupRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
	private val offlineBlocker: OfflineBlocker,
) : AppGroupRemoteDataSource {

	override fun getAppGroups(onError: suspend (Throwable) -> Unit): Flow<List<AppGroup>> = flow {
		offlineBlocker {
			retrofitBrakeApi.getAppGroups()
				.suspendOnSuccess {
					emit(data.data.groups.map(AppGroupData::toAppGroup))
				}.suspendOnFailure {
					onError(Throwable("앱 그룹을 가져오는 중 오류가 발생했습니다"))
					Timber.e("Error fetching app groups: $this")
				}
		}
	}

	override fun createAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroup> = flow {
		offlineBlocker {
			retrofitBrakeApi.createAppGroup(appGroup.toAppGroupRequest())
				.suspendOnSuccess {
					emit(data.data.toAppGroup())
				}.suspendOnFailure {
					onError(Throwable("앱 그룹을 생성하는 중 오류가 발생했습니다"))
					Timber.e("Error creating app group: $this")
				}
		}
	}

	override fun updateAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroup> = flow {
		offlineBlocker {
			retrofitBrakeApi.updateAppGroup(appGroup.id, appGroup.toAppGroupRequest())
				.suspendOnSuccess {
					emit(data.data.toAppGroup())
				}.suspendOnFailure {
					onError(Throwable("앱 그룹을 수정하는 중 오류가 발생했습니다"))
					Timber.e("Error updating app group: $this")
				}
		}
	}

	override suspend fun deleteAppGroup(
		groupId: Long,
		onSuccess: suspend () -> Unit,
		onError: suspend (Throwable) -> Unit,
	) {
		offlineBlocker {
			retrofitBrakeApi.deleteAppGroup(groupId)
				.suspendOnSuccess {
					onSuccess()
				}.suspendOnFailure {
					onError(Throwable("앱 그룹을 삭제하는 중 오류가 발생했습니다"))
					Timber.e("Error deleting app group: $this")
				}
		}
	}
}
