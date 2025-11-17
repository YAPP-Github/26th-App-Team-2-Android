package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.UserLocalDataSource
import com.teambrake.brake.data.remote.source.NameRemoteDataSource
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.data.repository.util.OfflineBlocker
import com.teambrake.brake.data.repository.util.OfflineException
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class NicknameRepositoryImpl @Inject constructor(
	private val offlineBlocker: OfflineBlocker,
	private val nameRemoteDataSource: NameRemoteDataSource,
	private val userLocalDataSource: UserLocalDataSource,
) : NicknameRepository {

	override fun getRemoteUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> =
		offlineBlocker.blockFlow {
			nameRemoteDataSource.getUserName(onError = onError)
		}.map {
			it.toData()
		}.catch { exception ->
			if (exception is OfflineException) {
				onError(exception)
			}
			throw exception
		}

	override fun getLocalUserName(onError: suspend (Throwable) -> Unit): Flow<String> =
		userLocalDataSource.getNickname(onError = onError)

	override suspend fun saveLocalUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	) {
		userLocalDataSource.updateNickname(
			nickname = nickname,
			onError = onError,
		)
	}

	override fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName> = offlineBlocker.blockFlow {
		nameRemoteDataSource.updateUserName(
			nickname = nickname,
			onError = onError,
		)
	}.onEach {
		// 새로운 닉네임을 로컬에 저장
		userLocalDataSource.updateNickname(nickname, onError = onError)
	}.map {
		it.toData()
	}.catch {
		if (it is OfflineException) {
			// 오프라인 모드의 경우에도 닉네임을 로컬에 저장
			// suspend 함수이므로 flow 빌더 내에서 처리하나 추후 수정 고려
			userLocalDataSource.updateNickname(nickname, onError = onError)
			emit(
				UserName(
					nickname = nickname,
					state = UserStatus.OFFLINE,
				),
			)
		}
	}

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		userLocalDataSource.clearNickname(onError = onError)
	}
}
