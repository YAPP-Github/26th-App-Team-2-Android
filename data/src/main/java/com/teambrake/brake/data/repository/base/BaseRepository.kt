package com.teambrake.brake.data.repository.base

import com.teambrake.brake.data.local.source.TokenLocalDataSource

open class BaseRepository(
	private val tokenLocalDataSource: TokenLocalDataSource,
) {
	// TODO: 모든 repository에서 local 과 reemote 처리
}
