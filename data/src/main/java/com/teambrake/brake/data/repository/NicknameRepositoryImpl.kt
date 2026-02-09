package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.NameLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.NameRemoteDataSource
import com.teambrake.brake.data.repository.base.BaseRepository
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.domain.model.exception.LocalException
import com.teambrake.brake.domain.model.exception.NetworkException
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
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
					throw e
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
			Timber.e("로컬에 닉네임 저장 실패")
			Result.failure(LocalException(Exception("failed to save nickname locally")))
		}
	} catch (e: Exception) {
		Timber.e(e, "로컬에 닉네임 저장 중 예외 발생")
		Result.failure(LocalException(e))
	}

	override suspend fun updateUserName(
		nickname: String,
	): Result<UserName> = if (isOnlineStatus()) {
		try {
			nameRemoteDataSource.updateUserName(
				nickname = nickname,
				onError = { e ->
					Timber.e("닉네임 업데이트 실패: $e")
					throw e
				},
			).map {
				it.toData()
			}.firstOrNull()?.let { userName ->
				// 원격 저장 성공 시 로컬에 저장
				val localSaveSuccess = nameLocalDataSource.updateNickname(nickname)
				if (!localSaveSuccess) {
					Timber.e("원격 업데이트 후 로컬 저장 실패")
					Result.failure(LocalException(Exception("failed to save nickname locally after remote update")))
				} else {
					Result.success(userName)
				}
			} ?: run {
				Timber.e("원격에서 데이터를 받지 못함")
				Result.failure(NetworkException("No data emitted from remote"))
			}
		} catch (e: IOException) {
			Timber.e(e, "네트워크 오류로 닉네임 업데이트 실패")
			Result.failure(NetworkException("닉네임 업데이트 중 네트워크 오류 발생"))
		} catch (exception: Exception) {
			Timber.e(exception, "닉네임 원격 업데이트 실패")
			Result.failure(LocalException(exception))
		}
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
			Timber.e("오프라인 모드에서 로컬 저장 실패")
			Result.failure(LocalException(Exception("failed to save nickname locally in offline mode")))
		}
	}

	override suspend fun clearLocalName(): Result<Unit> = try {
		val result = nameLocalDataSource.clearNickname()
		if (result) {
			Result.success(Unit)
		} else {
			Timber.e("로컬 닉네임 삭제 실패")
			Result.failure(LocalException(Exception("failed to clear local nickname")))
		}
	} catch (e: Exception) {
		Timber.e(e, "로컬 닉네임 삭제 중 예외 발생")
		Result.failure(LocalException(e))
	}
}
