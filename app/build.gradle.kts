import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

plugins {
	alias(libs.plugins.breake.android.application)
	alias(libs.plugins.breake.work.hilt)
	id("com.google.android.gms.oss-licenses-plugin")
	alias(libs.plugins.google.services)
	alias(libs.plugins.firebase.crashlytics)
	alias(libs.plugins.baselineprofile)
	alias(libs.plugins.roborazzi.plugin)
}

val releaseKeyFile = gradleLocalProperties(rootDir, providers)
	.getProperty("RELEASE_KEY_FILE")
if (releaseKeyFile.isNullOrEmpty()) {
	throw IllegalArgumentException("RELEASE_KEY_FILE must be set in local.properties")
}

val releaseStorePassword = gradleLocalProperties(rootDir, providers)
	.getProperty("RELEASE_STORE_PASSWORD")
if (releaseStorePassword.isNullOrEmpty()) {
	throw IllegalArgumentException("RELEASE_STORE_PASSWORD must be set in local.properties")
}

val releaseKeyAlias = gradleLocalProperties(rootDir, providers)
	.getProperty("RELEASE_KEY_ALIAS")
if (releaseKeyAlias.isNullOrEmpty()) {
	throw IllegalArgumentException("RELEASE_KEY_ALIAS must be set in local.properties")
}

val releaseKeyPassword = gradleLocalProperties(rootDir, providers)
	.getProperty("RELEASE_KEY_PASSWORD")
if (releaseKeyPassword.isNullOrEmpty()) {
	throw IllegalArgumentException("RELEASE_KEY_PASSWORD must be set in local.properties")
}

android {
	namespace = "com.yapp.breake"

	defaultConfig {
		applicationId = "com.yapp.breake"
	}
	signingConfigs {
		create("release") {
			// 배포 앱 스토어에 맞춰 local.properties 에서 파일 path 변경
			storeFile = file(releaseKeyFile)
			storePassword = releaseStorePassword
			keyAlias = releaseKeyAlias
			keyPassword = releaseKeyPassword
		}
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
			excludes += "META-INF/LICENSE.md"
		}
	}
	buildTypes {
		release {
			isMinifyEnabled = true
			isShrinkResources = true
			isDebuggable = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
			signingConfig = signingConfigs.getByName("release")
		}
	}

	applicationVariants.all {
		val variant = this
		variant.outputs
			.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
			.forEach { output ->
				val currentTime = SimpleDateFormat("yyyy.MM.dd HH-mm")
				currentTime.timeZone = TimeZone.getTimeZone("Asia/Seoul")
				val buildType = variant.buildType.name
				output.outputFileName = "[Brake_${buildType}_v${variant.versionName}]_${currentTime.format(Date())}.apk"
			}
	}

	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	implementation(projects.core.auth)
	implementation(projects.core.alarm)
	implementation(projects.core.detection)
	implementation(projects.core.navigation)
	implementation(projects.core.designsystem)
	implementation(projects.data)
	implementation(projects.dataTest)
	implementation(projects.domain)
	implementation(projects.presentation.main)
	implementation(projects.presentation.home)
	implementation(projects.overlay.main)

	implementation(platform(libs.firebase.bom))
	implementation(libs.firebase.analytics)
	implementation(libs.firebase.crashlytics)

	implementation(libs.androidx.profileinstaller)
	testImplementation(projects.core.testing)
}

tasks.register("printVersionName") {
	doLast {
		println(android.defaultConfig.versionName)
	}
}
