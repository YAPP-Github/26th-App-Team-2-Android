package com.yapp.breake.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.yapp.breake.core.datastore.serializer.AuthSerializer
import com.yapp.breake.core.datastore.model.DatastoreAuthCode
import com.yapp.breake.core.datastore.model.DatastoreOnboarding
import com.yapp.breake.core.datastore.model.DatastoreUserInfo
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.datastore.serializer.OnboardingSerializer
import com.yapp.breake.core.datastore.serializer.UserInfoSerializer
import com.yapp.breake.core.datastore.serializer.UserSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastoreModule {

	private val Context.TokenDataStore: DataStore<DatastoreUserToken> by dataStore(
		fileName = "user_token",
		serializer = UserSerializer,
	)

	private val Context.AuthDataStore: DataStore<DatastoreAuthCode> by dataStore(
		fileName = "auth_code",
		serializer = AuthSerializer,
	)

	private val Context.UserInfoDataStore: DataStore<DatastoreUserInfo> by dataStore(
		fileName = "user_info",
		serializer = UserInfoSerializer,
	)

	private val Context.OnboardingDataStore: DataStore<DatastoreOnboarding> by dataStore(
		fileName = "onboarding",
		serializer = OnboardingSerializer,
	)

	@Provides
	@Singleton
	fun provideUserTokenDataStore(
		@ApplicationContext context: Context,
	): DataStore<DatastoreUserToken> = context.TokenDataStore

	@Provides
	@Singleton
	fun provideAuthCodeDataStore(
		@ApplicationContext context: Context,
	): DataStore<DatastoreAuthCode> = context.AuthDataStore

	@Provides
	@Singleton
	fun provideUserInfoDataStore(
		@ApplicationContext context: Context,
	): DataStore<DatastoreUserInfo> = context.UserInfoDataStore

	@Provides
	@Singleton
	fun provideOnboardingDataStore(
		@ApplicationContext context: Context,
	): DataStore<DatastoreOnboarding> = context.OnboardingDataStore
}
