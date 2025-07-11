package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("UserProfileRepo") private val userProfileRepository: UserProfileRepository,
	@Named("FakeUserProfileRepo") private val fakeUserProfileRepository: UserProfileRepository,
) : UpdateNicknameUseCase {

	override fun invoke(nickname: String): Flow<ResponseResult<Unit>> =
		// TODO: 서버 안정화 후 네트워크 호출로 변경 예정
		fakeUserProfileRepository.updateUserProfile(nickname).map {
			when (it) {
				is ResponseResult.Success -> ResponseResult.Success(Unit)
				is ResponseResult.Error -> ResponseResult.Error(it.message)
				is ResponseResult.Exception -> ResponseResult.Exception(it.exception)
			}
		}
}
