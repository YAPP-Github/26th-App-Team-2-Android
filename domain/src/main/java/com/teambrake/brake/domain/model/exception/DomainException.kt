package com.teambrake.brake.domain.model.exception

/**
 * Domain Layer에서 사용되는 예외 클래스
 * Repository 레이어에서 기술적 예외를 Domain Exception으로 변환
 */
sealed class DomainException(message: String) : Exception(message)

/**
 * HTTP 통신 오류 (4xx, 5xx 등)
 * @param code HTTP 상태 코드
 * @param message 오류 메시지
 */
class HttpException(val code: Int, message: String) : DomainException(message)

/**
 * 로컬 데이터베이스/DataStore 오류
 * @param cause 원인 예외
 */
class LocalException(cause: Throwable) : DomainException(cause.message ?: "Local error")

/**
 * 네트워크 연결 오류 (서버 접근 불가)
 * @param message 오류 메시지
 */
class NetworkException(message: String) : DomainException(message)

/**
 * 정의되지 않은 예외
 * @param cause 원인 예외
 */
class UnknownException(cause: Throwable) : DomainException(cause.message ?: "Unknown error")
