package com.teambrake.brake.data.local.source

import androidx.datastore.core.DataStore
import com.teambrake.brake.core.datastore.model.DatastoreOnboarding
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class OnboardingLocalDataSourceImpl @Inject constructor(
	private val onboardingDataStore: DataStore<DatastoreOnboarding>,
) : OnboardingLocalDataSource {
	override suspend fun updateOnboardingFlag(isComplete: Boolean): Boolean = runCatching {
		onboardingDataStore.updateData { it.copy(flag = isComplete) }
	}.onFailure { e ->
		Timber.e(e, "Error updating onboarding flag")
	}.isSuccess

	override fun getOnboardingFlag(): Flow<Boolean> = flow {
		onboardingDataStore.data.collect { onboardingFlag ->
			emit(onboardingFlag.flag)
		}
	}
}
