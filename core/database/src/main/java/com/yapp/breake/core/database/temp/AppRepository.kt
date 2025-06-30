package com.yapp.breake.core.database.temp

interface AppRepository {

	suspend fun getAppGroupIdByPackage(packageName: String): Long?
}
