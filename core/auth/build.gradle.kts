import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	alias(libs.plugins.breake.android.compose)
}

android {
	setNamespace("core.auth")

	defaultConfig {
		// 인가 코드 받기: https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-code
		val kakaoRestApiKey =
			gradleLocalProperties(rootDir, providers).getProperty("KAKAO_REST_API_KEY")
		if (kakaoRestApiKey.isNullOrEmpty()) {
			throw IllegalArgumentException("KAKAO_REST_API_KEY must be set in local.properties")
		}
		buildConfigField("String", "KAKAO_REST_API_KEY", "\"$kakaoRestApiKey\"")

		// 간편로그인: https://developers.kakao.com/docs/latest/ko/kakaologin/js#login
		val kakaoJsKey =
			gradleLocalProperties(rootDir, providers).getProperty("KAKAO_JS_KEY")
		if (kakaoJsKey.isNullOrEmpty()) {
			throw IllegalArgumentException("KAKAO_JS_KEY must be set in local.properties")
		}
		buildConfigField("String", "KAKAO_JS_KEY", "\"$kakaoJsKey\"")
	}

	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	implementation(libs.kakao.user)
}
