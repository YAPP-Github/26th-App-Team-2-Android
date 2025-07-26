package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import javax.inject.Inject

internal class AccountRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : AccountRemoteDataSource {
	override suspend fun deleteAccount(onError: suspend (Throwable) -> Unit) {
		retrofitBrakeApi.deleteMemberName()
			.suspendOnSuccess {
				// 계정 삭제 성공 시 아무 작업도 하지 않음
			}
			.suspendOnFailure {
				onError(Throwable("계정을 삭제하는 중 오류가 발생했습니다"))
			}
	}

	override suspend fun logoutAccount(
		accessToken: String,
		onError: suspend (Throwable) -> Unit,
	) {
		retrofitBrakeApi.logoutAuth(accessToken)
			.suspendOnSuccess {
				// 로그아웃 성공 시 아무 작업도 하지 않음
			}
			.suspendOnFailure {
				onError(Throwable("로그아웃 중 오류가 발생했습니다"))
			}
	}
}
