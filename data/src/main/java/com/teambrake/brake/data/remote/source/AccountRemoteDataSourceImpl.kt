package com.teambrake.brake.data.remote.source

import com.skydoves.sandwich.retrofit.statusCode
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.teambrake.brake.data.remote.retrofit.RetrofitBrakeApi
import javax.inject.Inject

internal class AccountRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : AccountRemoteDataSource {
	override suspend fun deleteAccount(onError: suspend (Throwable) -> Unit) {
		retrofitBrakeApi.deleteMemberName()
			.suspendOnSuccess {
				// 계정 삭제 성공 시 아무 작업도 하지 않음
			}
			.suspendOnError {
				when (statusCode.code) {
					in 400..499 -> {
						// 서버에서 이미 삭제된 계정에 대한 요청이 들어온 경우
					}

					else -> {
						onError(
							Throwable("서버 오류로 계정을 삭제할 수 없습니다."),
						)
					}
				}
			}
			.suspendOnException {
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
