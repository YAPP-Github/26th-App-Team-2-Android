package com.yapp.breake.data.network.di

import com.yapp.breake.data.network.BrakeNetwork
import com.yapp.breake.data.network.BrakeNetworkImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import timber.log.Timber
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.collections.forEach

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

	@Provides
	@Singleton
	fun provideOkhttpClient(
		interceptors: Provider<Set<@JvmSuppressWildcards Interceptor>>,
	): OkHttpClient =
		OkHttpClient.Builder()
			.apply {
				interceptors.get().forEach { addInterceptor(it) }
			}
			.build()

	@Provides
	@Singleton
	@IntoSet
	fun providerHttpLoggingInterceptor(): Interceptor =
		HttpLoggingInterceptor {
			Timber.d("BrakeNetwork: $it")
		}.apply {
			level = HttpLoggingInterceptor.Level.BODY
		}

	@Provides
	@Singleton
	fun provideConverterFactory(
		json: Json,
	): Converter.Factory =
		json.asConverterFactory("application/json".toMediaType())

	@Provides
	fun provideRetrofitBuilder(
		okHttpClient: OkHttpClient,
		converterFactory: Converter.Factory,
	): Retrofit.Builder =
		Retrofit.Builder()
			.addConverterFactory(converterFactory)
			.client(okHttpClient)

	@Provides
	fun provideBrakeNetwork(
		retrofitBuilder: Retrofit.Builder,
	): BrakeNetwork =
		BrakeNetworkImpl(retrofitBuilder)

	@Provides
	@Singleton
	fun provideJson(): Json =
		Json {
			ignoreUnknownKeys = true
			coerceInputValues = true
			encodeDefaults = true
		}
}
