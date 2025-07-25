package com.breake.core.permission

import android.content.Context
import android.content.Intent

interface PermissionManager {
	fun isAllGranted(context: Context): Boolean
	fun isGranted(context: Context, permissionType: PermissionType): Boolean
	fun getIntent(context: Context, permissionType: PermissionType): Intent
}
