package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.data.remote.model.MemberRequest
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import com.yapp.breake.data.repository.mapper.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class NameRemoteDataSource @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) {
	fun updateUserName(
		accessToken: String,
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName> = flow {
		retrofitBrakeApi.updateMemberName("Bearer $accessToken", MemberRequest(nickname))
			.suspendOnSuccess {
				emit(this.data.toData())
			}.suspendOnFailure {
				onError(Throwable("유저 닉네임을 등록하는 중 오류가 발생했습니다"))
			}
	}

	fun getUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> = flow {
		retrofitBrakeApi.getMemberName()
			.suspendOnSuccess {
				emit(this.data.toData())
			}.suspendOnFailure {
				onError(Throwable("유저 정보를 가져오는 중 오류가 발생했습니다"))
			}
	}
}
