package com.yapp.breake.data.repository

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.data.api.LoginApi
import com.yapp.breake.data.api.model.LoginRequest
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.data.util.safeNetworkCall
import com.yapp.breake.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
	private val loginApi: LoginApi,
) : LoginRepository {

	override fun flowLogin(
		provider: String,
		authorizationCode: String,
	): Flow<ResponseResult<UserToken>> = safeNetworkCall(
		apiCall = {
			loginApi.getTokens(
				LoginRequest(
					provider = provider,
					authorizationCode = authorizationCode,
				),
			)
		},
		mapper = { it.toData() },
		predicateOnSuccess = {
			it.toData().run {
				accessToken.isNotBlank() &&
					refreshToken.isNotBlank() &&
					// 서버에서는 status를 ACTIVE, HALF_SINGUP 두 가지만 전달
					// 혹여나 나머지 상태가 감지되는 경우, INACTIVE로 간주
					status != UserTokenStatus.INACTIVE
			}
		},
	)
}
