package com.yapp.breake.core.detection

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface CachedModule {

	@Binds
	fun bindCachedDatabase(
		cachedDatabaseImpl: CachedDatabaseImpl,
	): CachedDatabase
}
