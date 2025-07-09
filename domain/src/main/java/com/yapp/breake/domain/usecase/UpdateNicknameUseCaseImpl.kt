package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("UserProfileRepo") private val userProfileRepository: UserProfileRepository,
	@Named("FakeUserProfileRepo") private val fakeUserProfileRepository: UserProfileRepository,
) : UpdateNicknameUseCase {

	@OptIn(ExperimentalCoroutinesApi::class)
	override suspend fun invoke(nickname: String): Flow<Unit> = flow {
		// TODO: 서버 안정화 후 네트워크 호출로 변경 예정
		fakeUserProfileRepository.updateUserProfile(nickname).first().let {
			emit(Unit)
		}
	}
}
