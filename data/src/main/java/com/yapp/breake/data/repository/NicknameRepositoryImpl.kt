package com.yapp.breake.data.repository

import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.data.local.source.UserLocalDataSource
import com.yapp.breake.data.remote.source.NameRemoteDataSource
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class NicknameRepositoryImpl @Inject constructor(
	private val nameRemoteDataSource: NameRemoteDataSource,
	private val userLocalDataSourceImpl: UserLocalDataSource,
) : NicknameRepository {

	override fun getRemoteUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> =
		nameRemoteDataSource.getUserName(onError = onError).map { it.toData() }

	override fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName> = nameRemoteDataSource.updateUserName(
		nickname = nickname,
		onError = onError,
	).onEach {
		// 새로운 닉네임을 로컬에 저장
		userLocalDataSourceImpl.updateNickname(nickname, onError = onError)
	}.map { it.toData() }

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		userLocalDataSourceImpl.clearNickname(onError = onError)
	}
}
