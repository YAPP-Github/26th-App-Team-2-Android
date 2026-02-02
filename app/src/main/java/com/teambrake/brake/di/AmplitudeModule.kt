package com.teambrake.brake.di

import android.content.Context
import com.amplitude.android.Amplitude
import com.amplitude.android.TrackingOptions
import com.teambrake.brake.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AmplitudeModule {

	@Provides
	@Singleton
	fun provideAmplitudeInstance(@ApplicationContext context: Context): Amplitude = Amplitude(
		com.amplitude.android.Configuration(
			apiKey = BuildConfig.AMPLITUDE_API_KEY,
			context = context,
			trackingOptions = TrackingOptions().apply {
				disableAdid()	// 광고 ID 차단
				disableLatLng()	// 위치 정보 차단
				disableIpAddress()	// IP 차단
				disableCarrier()	// 통신사 정보 차단
			},
			flushIntervalMillis = 10000,	// 10초마다 Amplitude 전송
		),
	)
}
