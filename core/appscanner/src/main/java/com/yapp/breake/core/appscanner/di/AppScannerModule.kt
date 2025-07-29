package com.yapp.breake.core.appscanner.di

import android.content.Context
import com.yapp.breake.core.appscanner.InstalledAppScanner
import com.yapp.breake.core.appscanner.InstalledAppScannerImpl
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
