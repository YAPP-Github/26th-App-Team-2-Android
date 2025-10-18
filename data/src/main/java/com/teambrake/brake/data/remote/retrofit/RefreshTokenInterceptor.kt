package com.teambrake.brake.data.remote.retrofit

import androidx.datastore.core.DataStore
import com.teambrake.brake.core.datastore.model.DatastoreUserToken
import com.teambrake.brake.data.remote.model.RefreshRequest
import com.teambrake.brake.data.remote.model.RefreshResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import timber.log.Timber

class RefreshTokenInterceptor(
	private val userTokenDataSource: DataStore<DatastoreUserToken>,
) : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		val originalResponse = chain.proceed(originalRequest)

		// 헤더가 AccessToken 인 요청만을 대상으로 토큰 재발급 시도
		if (!originalRequest.url.encodedPath.startsWith("/v1/auth") &&
			originalResponse.code in (400..499)
		) {
			val originalRefreshToken = runBlocking {
				userTokenDataSource.data.first().refreshToken
			}

			if (originalRefreshToken != null) {
				// 토큰 재발급 요청 및 응답 처리를 위한 작업
				val mediaType = "application/json; charset=utf-8".toMediaType()
				val retryRefresh = originalRequest.newBuilder()
					.url(
						originalRequest.url.newBuilder()
							.encodedPath("/v1/auth/refresh")
							.query(null)
							.build(),
					)
					.method(
						"POST",
						Json.encodeToString(RefreshRequest(originalRefreshToken))
							.toRequestBody(mediaType),
					)
					// 기존 헤더 제거
					.removeHeader("Authorization")
					.build()

				// 요청 전 Response 리소스 해제
				originalResponse.close()

				// 토큰 재발급 요청 및 응답 처리
				val refreshResponse = chain.proceed(retryRefresh)
				Timber.d(
					"RefreshTokenInterceptor: Refresh Tokens response code: ${refreshResponse.code}",
				)
				if (refreshResponse.isSuccessful) {
					val responseBody = refreshResponse.body?.string()

					responseBody?.let { body ->
						val refreshData = Json.decodeFromString<RefreshResponse>(body)
						val newAccessToken = refreshData.data.accessToken
						val newRefreshToken = refreshData.data.refreshToken

						Timber.d(
							"TokenRefreshInterceptor: New tokens received - Access: $newAccessToken, Refresh: $newRefreshToken",
						)

						runBlocking {
							userTokenDataSource.updateData { tokenObject ->
								tokenObject.copy(
									accessToken = newAccessToken,
									refreshToken = newRefreshToken,
								)
							}
						}
						// 토큰 재발급 요청의 response 의 body 가 null인 경우 response 반환
					} ?: return refreshResponse

					val newAccessToken = runBlocking {
						userTokenDataSource.data.first().accessToken
					}
					// originalRequest 기반으로 최초 실패 요청 재시도
					val retryRequest = originalRequest.newBuilder()
						.removeHeader("Authorization")
						.header("Authorization", "Bearer $newAccessToken")
						.build()

					refreshResponse.close()
					return chain.proceed(retryRequest)
				} else {
					return refreshResponse
				}
			}
		}

		return originalResponse
	}
}
