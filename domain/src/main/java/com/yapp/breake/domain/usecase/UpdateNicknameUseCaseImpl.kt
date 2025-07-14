package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("UserProfileRepo") private val userProfileRepository: UserProfileRepository,
	@Named("FakeUserProfileRepo") private val fakeUserProfileRepository: UserProfileRepository,
) : UpdateNicknameUseCase {

	override suspend fun invoke(nickname: String, onError: suspend (Throwable) -> Unit) {
		// TODO: 서버 안정화 후 네트워크 호출로 변경 예정
		userProfileRepository.updateUserProfile(nickname, onError).first()
	}
}
