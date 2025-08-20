package com.yapp.breake.data.remote.retrofit.di

import androidx.datastore.core.DataStore
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.data.remote.retrofit.ApiConfig
import com.yapp.breake.data.remote.retrofit.HeaderSelectionInterceptor
import com.yapp.breake.data.remote.retrofit.HttpNetworkLogger
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import com.yapp.breake.data.remote.retrofit.RetryTimeoutInterceptor
import com.yapp.breake.data.remote.retrofit.RefreshTokenInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

	@Provides
	@Singleton
	fun provideOkhttpClient(
		interceptors: Provider<Set<@JvmSuppressWildcards Interceptor>>,
		userTokenDataSource: DataStore<DatastoreUserToken>,
	): OkHttpClient =
		OkHttpClient.Builder()
			.apply {
				interceptors.get().forEach(::addInterceptor)
			}
			.connectTimeout(10, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.readTimeout(30, TimeUnit.SECONDS)
			.addInterceptor(RetryTimeoutInterceptor(maxRetries = 1))
			.addInterceptor(
				RefreshTokenInterceptor(
					userTokenDataSource = userTokenDataSource,
				),
			)
			.build()

	@Provides
	@Singleton
	@IntoSet
	fun providerHttpLoggingInterceptor(): Interceptor =
		HttpNetworkLogger()

	@Provides
	@Singleton
	@IntoSet
	fun provideHeaderSelectionInterceptor(
		tokenDataStore: DataStore<DatastoreUserToken>,
	): Interceptor = HeaderSelectionInterceptor(tokenDataStore)

	@Provides
	@Singleton
	fun provideConverterFactory(
		json: Json,
	): Converter.Factory =
		json.asConverterFactory("application/json".toMediaType())

	@Provides
	fun provideBrakeApi(
		okHttpClient: OkHttpClient,
		converterFactory: Converter.Factory,
	): RetrofitBrakeApi = Retrofit.Builder()
		.addConverterFactory(converterFactory)
		.addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
		.baseUrl(ApiConfig.ServerDomain.BASE_URL)
		.client(okHttpClient)
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
