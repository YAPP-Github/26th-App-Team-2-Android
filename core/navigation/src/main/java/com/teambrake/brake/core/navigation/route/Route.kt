package com.teambrake.brake.core.navigation.route

import timber.log.Timber

interface Route

/**
 * 2025-08-03 기준
 * Jetpack Compose Type-Safe Navigation 내부에서 Route 타입을 문자열로 변환하는 방식과 동일한 변환 함수
 * Jetpack Compose Type-Safe Navigation 내부 변환 방식이 변경될 수 있음
 */
fun Route.stringRoute(): String {
	Timber.d("stringRoute: ${this::class.qualifiedName}")
	return this::class.qualifiedName ?: ""
}
