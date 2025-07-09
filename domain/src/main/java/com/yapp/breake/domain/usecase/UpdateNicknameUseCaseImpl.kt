package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateNicknameUseCaseImpl @Inject constructor(
	private val userProfileRepository: UserProfileRepository,
) : UpdateNicknameUseCase {

	@OptIn(ExperimentalCoroutinesApi::class)
	override suspend fun invoke(nickname: String): Flow<Unit> = flow {
		try {
			userProfileRepository.updateUserProfile(nickname).first().let {
				emit(Unit)
			}
		} catch (e: Exception) {
			throw e
		}
	}
}
