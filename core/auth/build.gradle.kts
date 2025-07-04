import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
}

android {
	setNamespace("core.auth")

	defaultConfig {
		manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] =
			gradleLocalProperties(rootDir, providers).getProperty("KAKAO_NATIVE_APP_KEY")
	}
}

dependencies {
	implementation(libs.kakao.user)
}
