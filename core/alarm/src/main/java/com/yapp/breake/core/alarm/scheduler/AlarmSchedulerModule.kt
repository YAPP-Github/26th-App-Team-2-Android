package com.yapp.breake.core.alarm.scheduler

import android.app.AlarmManager
import android.content.Context
import com.yapp.breake.domain.repository.AppGroupRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AlarmSchedulerModule {

	@Provides
	@Singleton
	fun provideAlarmManager(
		@ApplicationContext context: Context,
	): AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

	@Provides
	@Singleton
	fun provideAlarmScheduler(
		alarmManager: AlarmManager,
		appGroupRepository: AppGroupRepository,
		@ApplicationContext context: Context,
	): AlarmScheduler = AlarmSchedulerImpl(
		alarmManager = alarmManager,
		appGroupRepository = appGroupRepository,
		context = context,
	)
}
