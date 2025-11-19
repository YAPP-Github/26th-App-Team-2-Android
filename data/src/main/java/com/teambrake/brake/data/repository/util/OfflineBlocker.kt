package com.teambrake.brake.data.repository.util

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

internal interface OfflineBlocker {
	@Throws(OfflineException::class)
	suspend fun <T> block(runIfOnline: suspend () -> T): T

	@Throws(OfflineException::class)
	fun <T> blockFlow(flowProvider: () -> Flow<T>): Flow<T>
}

internal class OfflineBlockerImpl @Inject constructor(
	private val tokenLocalDataSource: TokenLocalDataSource,
) : OfflineBlocker {
	@Throws(OfflineException::class)
	override suspend fun <T> block(runIfOnline: suspend () -> T): T {
		val status = tokenLocalDataSource.getUserStatus(
			onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
		).firstOrNull()
		return when (status) {
			UserStatus.OFFLINE -> {
				throw OfflineException("오프라인 모드입니다. 작업이 차단되었습니다.")
			}
			null -> {
				throw Exception()
			}
			else -> {
				runIfOnline()
			}
		}
	}

	@Throws(OfflineException::class)
	override fun <T> blockFlow(flowProvider: () -> Flow<T>): Flow<T> = flow {
		val status = tokenLocalDataSource.getUserStatus(
			onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
		).firstOrNull()
		when (status) {
			UserStatus.OFFLINE -> {
				throw OfflineException("오프라인 모드입니다. 작업이 차단되었습니다.")
			}
			null -> {
				throw Exception()
			}
			else -> {
				flowProvider().collect { emit(it) }
			}
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

/**
 * Repository layer 에서만 사용되는 오프라인 모드 예외 클래스
 */
internal class OfflineException(message: String) : Exception(message)
