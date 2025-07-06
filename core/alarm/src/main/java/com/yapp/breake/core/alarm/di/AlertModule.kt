package com.yapp.breake.core.alarm.di

import android.content.Context
import com.yapp.breake.core.alarm.scheduler.AlarmScheduler
import com.yapp.breake.core.alarm.scheduler.AlarmSchedulerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AlertModule {

	@Provides
	@Singleton
	fun bindAlarmScheduler(
		@ApplicationContext context: Context,
	): AlarmScheduler = AlarmSchedulerImpl(context)
}
