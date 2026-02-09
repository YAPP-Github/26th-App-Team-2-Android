package com.teambrake.brake.data.local.source

import kotlinx.coroutines.flow.Flow

interface OnboardingLocalDataSource {
	suspend fun updateOnboardingFlag(isComplete: Boolean): Boolean
	fun getOnboardingFlag(): Flow<Boolean>
}
