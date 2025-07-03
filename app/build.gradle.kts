import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
private val kakaoNativeAppKey: String =
	gradleLocalProperties(rootDir, providers).getProperty("KAKAO_NATIVE_APP_KEY")

plugins {
	alias(libs.plugins.breake.android.application)
	id("com.google.android.gms.oss-licenses-plugin")
	alias(libs.plugins.baselineprofile)
	alias(libs.plugins.roborazzi.plugin)
}

android {
	namespace = "com.yapp.breake"

	defaultConfig {
		applicationId = "com.yapp.breake"

		buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${kakaoNativeAppKey}\"")
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
			excludes += "META-INF/LICENSE.md"
		}
	}
	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
			signingConfig = signingConfigs.getByName("debug")
		}
	}
	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	implementation(projects.core.auth)
	implementation(projects.core.navigation)
	implementation(projects.core.designsystem)
	implementation(projects.data)
	implementation(projects.presentation.main)
	implementation(projects.presentation.home)

	implementation(libs.androidx.profileinstaller)
	implementation(libs.kakao.user)

	testImplementation(projects.core.testing)
}
