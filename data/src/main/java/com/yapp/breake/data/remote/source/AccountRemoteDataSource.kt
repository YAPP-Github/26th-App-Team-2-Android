package com.yapp.breake.data.remote.source

interface AccountRemoteDataSource {
	suspend fun deleteAccount(
		onError: suspend (Throwable) -> Unit,
	)

	suspend fun logoutAccount(
		accessToken: String,
		onError: suspend (Throwable) -> Unit,
	)
}
