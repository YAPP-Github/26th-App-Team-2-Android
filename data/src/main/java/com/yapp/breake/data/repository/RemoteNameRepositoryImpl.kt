package com.yapp.breake.data.repository

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.data.api.MemberApi
import com.yapp.breake.data.api.model.MemberRequest
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.domain.repository.RemoteNameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RemoteNameRepositoryImpl @Inject constructor(
	private val memberApi: MemberApi,
) : RemoteNameRepository {

	override fun getUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> = flow {
		memberApi.getMemberInfo()
			.suspendOnSuccess {
				emit(this.data.toData())
			}.suspendOnFailure {
				onError(Throwable("유저 정보를 가져오는 중 오류가 발생했습니다"))
			}
	}

	override fun updateUserName(
		authCode: String,
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName> = flow {
		memberApi.updateMemberInfo(authCode, MemberRequest(nickname))
			.suspendOnSuccess {
				emit(this.data.toData())
			}.suspendOnFailure {
				onError(Throwable("유저 닉네임을 등록하는 중 오류가 발생했습니다"))
			}
	}
}
