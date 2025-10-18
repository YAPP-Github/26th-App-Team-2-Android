package com.teambrake.brake.core.alarm.di

import android.app.AlarmManager
import android.content.Context
import com.teambrake.brake.core.alarm.scheduler.AlarmSchedulerImpl
import com.teambrake.brake.domain.repository.AlarmScheduler
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
		@ApplicationContext context: Context,
	): AlarmScheduler = AlarmSchedulerImpl(
		alarmManager = alarmManager,
		context = context,
	)
}
