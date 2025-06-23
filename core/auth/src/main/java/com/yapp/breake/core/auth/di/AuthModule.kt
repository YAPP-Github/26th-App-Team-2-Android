package com.yapp.breake.core.auth.di

import com.yapp.breake.core.auth.KakaoAuthSDK
import com.yapp.breake.core.auth.LoginAuthSDK
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
internal abstract class AuthModule {

	@Named("kakaoAuthSDK")
	@Binds
	abstract fun bindKakaoAuthSDK(
		kakaoAuthSDK: KakaoAuthSDK
	): LoginAuthSDK
}
