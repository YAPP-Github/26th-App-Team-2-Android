package com.yapp.breake.data.api.di

import com.yapp.breake.data.api.ApiConfig
import com.yapp.breake.data.api.LoginApi
import com.yapp.breake.data.api.MemberApi
import com.yapp.breake.data.network.BrakeNetwork
import com.yapp.breake.data.network.create
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ApiModule {

	@Provides
	@Singleton
	fun provideLoginApi(
		brakeNetwork: BrakeNetwork,
	): LoginApi =
		brakeNetwork.create(
			baseUrl = ApiConfig.ServerDomain.BASE_URL,
		)

	@Provides
	@Singleton
	fun provideMemberApi(
		brakeNetwork: BrakeNetwork,
	): MemberApi =
		brakeNetwork.create(
			baseUrl = ApiConfig.ServerDomain.BASE_URL,
		)
}
