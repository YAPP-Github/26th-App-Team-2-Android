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

android {
	namespace = "com.yapp.breake"

	defaultConfig {
		applicationId = "com.yapp.breake"
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
			signingConfig = signingConfigs.getByName("debug")
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
