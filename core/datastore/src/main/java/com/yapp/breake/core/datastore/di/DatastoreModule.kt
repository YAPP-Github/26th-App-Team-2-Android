package com.yapp.breake.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.datastore.serializer.UserSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatastoreModule {

	private val Context.TokenDataStore: DataStore<DatastoreUserToken> by dataStore(
		fileName = "user_token",
		serializer = UserSerializer,
	)

	@Provides
	@Singleton
	@Named("UserToken")
	fun provideUserTokenDataStore(
		@ApplicationContext context: Context,
	): DataStore<DatastoreUserToken> = context.TokenDataStore
}
