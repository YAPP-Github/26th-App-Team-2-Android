import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	alias(libs.plugins.breake.android.compose)
}

android {
	setNamespace("core.auth")

	buildTypes {
		debug {
			val debugKakaoRestApiKey = gradleLocalProperties(rootDir, providers)
				.getProperty("KAKAO_REST_API_KEY_DEBUG")
			if (debugKakaoRestApiKey.isNullOrEmpty()) {
				throw IllegalArgumentException("KAKAO_REST_API_KEY_DEBUG must be set in local.properties")
			}
			buildConfigField("String", "KAKAO_REST_API_KEY", "\"$debugKakaoRestApiKey\"")

			val debugKakaoJsKey = gradleLocalProperties(rootDir, providers)
				.getProperty("KAKAO_JS_KEY_DEBUG")
			if (debugKakaoJsKey.isNullOrEmpty()) {
				throw IllegalArgumentException("KAKAO_JS_KEY_DEBUG must be set in local.properties")
			}
			buildConfigField("String", "KAKAO_JS_KEY", "\"$debugKakaoJsKey\"")
		}

		release {
			val releaseKakaoRestApiKey = gradleLocalProperties(rootDir, providers)
				.getProperty("KAKAO_REST_API_KEY_RELEASE")
			if (releaseKakaoRestApiKey.isNullOrEmpty()) {
				throw IllegalArgumentException("KAKAO_REST_API_KEY_RELEASE must be set in local.properties")
			}
			buildConfigField("String", "KAKAO_REST_API_KEY", "\"$releaseKakaoRestApiKey\"")

			val releaseKakaoJsKey = gradleLocalProperties(rootDir, providers)
				.getProperty("KAKAO_JS_KEY_RELEASE")
			if (releaseKakaoJsKey.isNullOrEmpty()) {
				throw IllegalArgumentException("KAKAO_JS_KEY_RELEASE must be set in local.properties")
			}
			buildConfigField("String", "KAKAO_JS_KEY", "\"$releaseKakaoJsKey\"")
		}
	}

	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	implementation(libs.kakao.user)

	// BackHandler 사용을 위한 의존성
	implementation(libs.androidx.activity.compose)

	// Google Authorization Login
	implementation(libs.google.auth)

	// Credential
	implementation(libs.androidx.credentials)
}
