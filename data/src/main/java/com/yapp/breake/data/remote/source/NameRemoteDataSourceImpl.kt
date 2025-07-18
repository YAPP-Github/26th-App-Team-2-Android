package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.data.remote.model.MemberRequest
import com.yapp.breake.data.remote.model.MemberResponse
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class NameRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : NameRemoteDataSource {
	override fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<MemberResponse> = flow {
		retrofitBrakeApi.updateMemberName(MemberRequest(nickname))
			.suspendOnSuccess {
				emit(this.data)
			}.suspendOnFailure {
				onError(Throwable("유저 닉네임을 등록하는 중 오류가 발생했습니다"))
			}
	}

	override fun getUserName(onError: suspend (Throwable) -> Unit): Flow<MemberResponse> = flow {
		retrofitBrakeApi.getMemberName()
			.suspendOnSuccess {
				emit(this.data)
			}.suspendOnFailure {
				onError(Throwable("유저 정보를 가져오는 중 오류가 발생했습니다"))
			}
	}
}
