package com.teambrake.brake.core.navigation.route

/**
 * 네비게이션 시 백스택 관리 방식을 정의하는 LaunchMode
 *
 * Android의 launchMode와 유사한 개념으로 설계됨
 */
enum class LaunchMode {
	/**
	 * 항상 새로운 인스턴스를 백스택에 추가
	 * 기본 동작
	 */
	STANDARD,

	/**
	 * 대상 Route가 이미 백스택의 최상단에 있으면 재사용
	 * 그렇지 않으면 새로 추가
	 */
	SINGLE_TOP,

	/**
	 * 백스택에서 대상 Route를 찾아 그 위의 모든 Route를 제거
	 * Route가 백스택에 없으면 새로 추가
	 */
	CLEAR_TOP,

	/**
	 * 백스택을 완전히 비우고 새로운 Route만 남김
	 */
	CLEAR_ALL,
}
