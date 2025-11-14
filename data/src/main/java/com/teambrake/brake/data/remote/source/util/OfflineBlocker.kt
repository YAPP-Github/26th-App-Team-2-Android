package com.teambrake.brake.data.remote.source.util

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Singleton

internal fun interface OfflineBlocker {
	suspend operator fun invoke(block: suspend () -> Unit)
}

internal class OfflineBlockerImpl @Inject constructor(
	private val tokenLocalDataSource: TokenLocalDataSource,
) : OfflineBlocker {
	override suspend fun invoke(block: suspend () -> Unit) {
		val status = tokenLocalDataSource.getUserStatus(
			onError = { /* 상태를 가져오는 중 오류가 발생해도 무시합니다 */ },
		).firstOrNull()
		if (status == UserStatus.OFFLINE) {
			block()
		}
	}
}

@Module
@InstallIn(SingletonComponent::class)
internal object OfflineBlockerModule {
	@Provides
	@Singleton
	fun provideOfflineBlocker(impl: OfflineBlockerImpl): OfflineBlocker = impl
}
