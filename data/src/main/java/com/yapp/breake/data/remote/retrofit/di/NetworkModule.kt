package com.yapp.breake.data.remote.retrofit.di

import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.yapp.breake.data.remote.retrofit.ApiConfig
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
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
			.addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
			.client(okHttpClient)

	@Provides
	@Singleton
	fun provideBrakeApi(
		retrofitBuilder: Retrofit.Builder,
	): RetrofitBrakeApi = retrofitBuilder
		.baseUrl(ApiConfig.ServerDomain.BASE_URL)
		.build()
		.create(RetrofitBrakeApi::class.java)

	@Provides
	@Singleton
	fun provideJson(): Json =
		Json {
			ignoreUnknownKeys = true
			coerceInputValues = true
			encodeDefaults = true
		}
}
