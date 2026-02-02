package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.NameLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.NameRemoteDataSource
import com.teambrake.brake.data.repository.base.BaseRepository
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class NicknameRepositoryImpl @Inject constructor(
	tokenLocalDataSource: TokenLocalDataSource,
	private val nameRemoteDataSource: NameRemoteDataSource,
	private val nameLocalDataSource: NameLocalDataSource,
) : BaseRepository(tokenLocalDataSource),
	NicknameRepository {

	override fun getNickname(): Flow<String> = flow {
		if (isOnlineStatus()) {
			// 온라인 상태: 원격에서 닉네임 가져오기
			emitAll(
				nameRemoteDataSource.getUserName { e ->
					throw Exception(e)
				}.map {
					it.toData().nickname
				},
			)
		} else {
			// 오프라인 상태: 로컬에서 닉네임 가져오기
			emitAll(nameLocalDataSource.getNickname())
		}
	}

	override suspend fun saveLocalUserName(
		nickname: String,
	): Result<Unit> = try {
		val result = nameLocalDataSource.updateNickname(nickname = nickname)
		if (result) {
			Result.success(Unit)
		} else {
			Result.failure(Exception("failed to save nickname locally"))
		}
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun updateUserName(
		nickname: String,
	): Result<UserName> = if (isOnlineStatus()) {
		// 온라인 상태: 원격 업데이트 후 로컬 저장
		var result: Result<UserName> = Result.failure(Exception("unexpected flow completion"))

		nameRemoteDataSource.updateUserName(
			nickname = nickname,
			onError = { throw it },
		).map {
			it.toData()
		}.collect { userName ->
			// 로컬에 저장
			val localSaveResult = nameLocalDataSource.updateNickname(nickname)
			result = if (!localSaveResult) {
				Result.failure(Exception("failed to save nickname locally"))
			} else {
				Result.success(userName)
			}
		}
		result
	} else {
		// 오프라인 상태: 로컬에만 저장
		val localSaveResult = nameLocalDataSource.updateNickname(nickname)
		if (localSaveResult) {
			Result.success(
				UserName(
					nickname = nickname,
					state = UserStatus.OFFLINE,
				),
			)
		} else {
			Result.failure(Exception("failed to save nickname locally"))
		}
	}

	override suspend fun clearLocalName(): Result<Unit> {
		val result = nameLocalDataSource.clearNickname()
		return if (result) {
			Result.success(Unit)
		} else {
			Result.failure(Exception("failed to clear local nickname"))
		}
	}
}
