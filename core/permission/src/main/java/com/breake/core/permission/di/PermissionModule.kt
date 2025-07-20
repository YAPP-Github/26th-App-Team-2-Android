package com.breake.core.permission.di

import com.breake.core.permission.PermissionManager
import com.breake.core.permission.PermissionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionModule {

	@Binds
	abstract fun bindPermissionManager(
		permissionManager: PermissionManagerImpl,
	): PermissionManager
}
