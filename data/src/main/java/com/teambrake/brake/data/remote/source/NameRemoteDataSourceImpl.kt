package com.teambrake.brake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.teambrake.brake.data.remote.model.MemberRequest
import com.teambrake.brake.data.remote.model.MemberResponse
import com.teambrake.brake.data.remote.retrofit.RetrofitBrakeApi
import com.teambrake.brake.data.remote.source.util.OfflineBlocker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class NameRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
	private val offlineBlocker: OfflineBlocker,
) : NameRemoteDataSource {
	override fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<MemberResponse> = flow {
		offlineBlocker {
			retrofitBrakeApi.updateMemberName(MemberRequest(nickname))
				.suspendOnSuccess {
					emit(this.data)
				}.suspendOnFailure {
					onError(Throwable("유저 닉네임을 등록하는 중 오류가 발생했습니다"))
				}
		}
	}

	override fun getUserName(onError: suspend (Throwable) -> Unit): Flow<MemberResponse> = flow {
		offlineBlocker {
			retrofitBrakeApi.getMemberName()
				.suspendOnSuccess {
					emit(this.data)
				}.suspendOnFailure {
					onError(Throwable("유저 정보를 가져오는 중 오류가 발생했습니다"))
				}
		}
	}
}
