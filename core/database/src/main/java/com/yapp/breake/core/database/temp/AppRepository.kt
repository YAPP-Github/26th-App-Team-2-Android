package com.yapp.breake.core.database.temp

import com.yapp.breake.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {

	fun observeApp(): Flow<List<App>>

	suspend fun getAppGroupIdByPackage(packageName: String): Long?
}
