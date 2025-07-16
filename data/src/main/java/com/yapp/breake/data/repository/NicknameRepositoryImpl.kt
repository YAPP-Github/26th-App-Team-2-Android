package com.yapp.breake.data.repository

import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.data.local.UserLocalDataSource
import com.yapp.breake.data.remote.source.NameRemoteDataSource
import com.yapp.breake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class NicknameRepositoryImpl @Inject constructor(
	private val nameRemoteDataSource: NameRemoteDataSource,
	private val userLocalDataSource: UserLocalDataSource,
) : NicknameRepository {

	override fun getRemoteUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> =
		nameRemoteDataSource.getUserName(onError = onError)

	override fun updateUserName(
		accessToken: String,
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName> = nameRemoteDataSource.updateUserName(
		accessToken = accessToken,
		nickname = nickname,
		onError = onError,
	).onEach {
		// 새로운 닉네임을 로컬에 저장
		userLocalDataSource.updateNickname(nickname, onError = onError)
	}

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		userLocalDataSource.clearNickname(onError = onError)
	}
}
