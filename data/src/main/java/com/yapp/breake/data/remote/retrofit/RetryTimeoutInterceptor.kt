package com.yapp.breake.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

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
			// Timeout 관련 예외는 재시도 가능
			is SocketTimeoutException -> true
			is ConnectException -> true
			is UnknownHostException -> true
			is TimeoutException -> true

			// 기타 IOException은 원인을 더 자세히 검사
			is IOException -> {
				// 원인(cause)이 Timeout 관련 예외면 재시도 가능
				val cause = e.cause
				when (cause) {
					is SocketTimeoutException -> true
					is TimeoutException -> true
					is ConnectException -> true
					else -> false
				}
			}
			else -> false
		}
	}
}
