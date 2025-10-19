package com.teambrake.brake.core.appscanner.di

import android.content.Context
import com.teambrake.brake.core.appscanner.InstalledAppScanner
import com.teambrake.brake.core.appscanner.InstalledAppScannerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppScannerModule {

	@Provides
	fun provideInstalledAppScanner(
		@ApplicationContext context: Context,
	): InstalledAppScanner = InstalledAppScannerImpl(context)
}
