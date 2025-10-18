package com.teambrake.brake.data.local.source

import com.teambrake.brake.core.appscanner.InstalledAppScanner
import com.teambrake.brake.core.database.dao.AppDao
import com.teambrake.brake.core.model.app.App
import com.teambrake.brake.data.mapper.toApp
import com.teambrake.brake.data.mapper.toAppEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class AppLocalDataSourceImpl @Inject constructor(
	private val appDao: AppDao,
	private val appScanner: InstalledAppScanner,
) : AppLocalDataSource {

	override suspend fun insertApp(
		parentGroupId: Long,
		app: App,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appDao.insert(app.toAppEntity(parentGroupId))
		} catch (e: Exception) {
			onError(Throwable("앱 저장에 실패했습니다"))
		}
	}

	override suspend fun insertApps(
		parentGroupId: Long,
		apps: List<App>,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			val appEntities = apps.map { it.toAppEntity(parentGroupId) }
			appDao.insertAll(appEntities)
		} catch (e: Exception) {
			onError(Throwable("앱 목록 저장에 실패했습니다"))
		}
	}

	override fun observeApp(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<App>> {
		return appDao.observeApps()
			.map { appEntities ->
				appEntities.map { it.toApp(appScanner) }
			}
			.catch { onError(Throwable("앱 목록 관찰에 실패했습니다")) }
	}

	override suspend fun getAppGroupIdByPackage(
		packageName: String,
		onError: suspend (Throwable) -> Unit,
	): Long? {
		return try {
			appDao.getAppByPackageName(packageName)?.parentGroupId
		} catch (e: Exception) {
			onError(Throwable("앱 그룹 ID 조회에 실패했습니다"))
			null
		}
	}

	override suspend fun deleteAppByParentGroupId(
		parentGroupId: Long,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appDao.deleteAppsByParentGroupId(parentGroupId)
		} catch (e: Exception) {
			onError(Throwable("앱 삭제에 실패했습니다"))
		}
	}

	override suspend fun clearApps(
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appDao.clearApps()
		} catch (e: Exception) {
			onError(Throwable("앱 목록 초기화에 실패했습니다"))
		}
	}
}
