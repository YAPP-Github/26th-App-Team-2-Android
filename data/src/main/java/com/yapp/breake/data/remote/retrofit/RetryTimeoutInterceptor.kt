package com.yapp.breake.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetryTimeoutInterceptor(private val maxRetries: Int) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()
		var tryCount = 0

		while (true) {
			try {
				Timber.d("Attempt: ${tryCount + 1}\nRequest URL: ${request.url}")
				val response = chain.proceed(request)

				return response
			} catch (e: Exception) {
				tryCount++

				if (canRetryException(e) && tryCount <= maxRetries) {
					Timber.w(e, "Retry Count: ($tryCount)")
					continue
				} else {
					Timber.e(e, "Request failed after $tryCount Retries")
					throw e
				}
			}
		}
	}

	private fun canRetryException(e: Exception): Boolean {
		return when (e) {
			// IP 주소를 찾을 수 없는 경우 재시도 제외
			is UnknownHostException -> false
			// Read, Write Timeout 관련 예외는 재시도 가능
			is SocketTimeoutException -> true

			// 기타 IOException은 원인을 더 자세히 검사
			is IOException -> {
				// 원인(cause)이 Timeout 관련 예외면 재시도 가능
				val cause = e.cause
				when (cause) {
					is SocketTimeoutException -> true
					else -> false
				}
			}
			else -> false
		}
	}
}
