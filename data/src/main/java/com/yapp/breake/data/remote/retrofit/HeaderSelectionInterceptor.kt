package com.yapp.breake.data.remote.retrofit

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderSelectionInterceptor @Inject constructor(
	private val tokenDataStore: DataStore<DatastoreUserToken>,
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {

		val path = chain.request().url.encodedPath
		val token = runBlocking {
			try {
				tokenDataStore.data.firstOrNull()?.accessToken
			} catch (_: Exception) {
				null
			}
		}

		val request = if (token != null) {
			if (!path.startsWith("/v1/auth/login")) {
				chain.request().newBuilder()
					.addHeader("Authorization", "Bearer $token")
					.build()
			} else {
				chain.request()
			}
		} else {
			chain.request()
		}
		return chain.proceed(request)
	}
}
