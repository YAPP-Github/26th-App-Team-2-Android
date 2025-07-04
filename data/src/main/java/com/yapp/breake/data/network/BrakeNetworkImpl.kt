package com.yapp.breake.data.network

import retrofit2.Retrofit

class BrakeNetworkImpl(
	private val retrofit: Retrofit.Builder,
) : BrakeNetwork {

	override fun <T> create(baseUrl: String, service: Class<T>): T =
		retrofit
			.baseUrl(baseUrl)
			.build()
			.create(service)
}
