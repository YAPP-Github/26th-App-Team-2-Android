package com.teambrake.brake.core.permission.di

import com.teambrake.brake.core.permission.PermissionManager
import com.teambrake.brake.core.permission.PermissionManagerImpl
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
