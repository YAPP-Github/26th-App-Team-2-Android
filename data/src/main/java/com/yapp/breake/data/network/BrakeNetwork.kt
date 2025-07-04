package com.yapp.breake.data.network

interface BrakeNetwork {
	fun <T> create(baseUrl: String, service: Class<T>): T
}

inline fun <reified T> BrakeNetwork.create(baseUrl: String): T {
	return create(baseUrl, T::class.java)
}
