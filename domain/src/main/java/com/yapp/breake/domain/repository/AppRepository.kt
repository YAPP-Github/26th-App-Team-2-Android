package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {

	fun observeApp(): Flow<List<App>>

	suspend fun getAppGroupIdByPackage(packageName: String): Long?
}
