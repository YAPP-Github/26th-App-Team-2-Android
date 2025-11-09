package com.teambrake.brake.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

internal class HttpNetworkLogger : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response {
		val request = chain.request()

		val logMessage = StringBuilder()
		logMessage.appendLine("\n${request.method} Request URL: ${request.url}")

		if (request.body != null) {
			val buffer = okio.Buffer()
			request.body!!.writeTo(buffer)
			logMessage.appendLine("Request Body: ${buffer.readUtf8()}")
		}

		val startTime = System.nanoTime()
		try {
			val response = chain.proceed(request)
			val endTime = System.nanoTime()

			val durationMs = (endTime - startTime) / 1e6
			logMessage.appendLine("${request.method} Response URL: ${response.request.url}")
			logMessage.appendLine("Response Code: ${response.code}")
			logMessage.appendLine("Response Time: ${durationMs}ms")

			val responseBody = response.body
			val contentLength = responseBody?.contentLength() ?: 0L

			if (contentLength != 0L) {
				val source = responseBody?.source()
				source?.request(Long.MAX_VALUE)
				val buffer = source?.buffer

				logMessage.appendLine(
					"Response Body: ${buffer?.clone()?.readString(Charsets.UTF_8)}",
				)
			}

			Timber.v(logMessage.toString())

			return response
		} catch (e: Exception) {
			val endTime = System.nanoTime()
			val durationMs = (endTime - startTime) / 1e6
			logMessage.appendLine("${request.method} Error occurred: ${e.message}")
			logMessage.appendLine("Response Time: ${durationMs}ms")

			Timber.e(logMessage.toString())
			throw e
		}
	}
}
