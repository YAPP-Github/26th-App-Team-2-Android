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
	implementation(projects.dataTest)
	implementation(projects.domain)
	implementation(projects.presentation.main)
	implementation(projects.presentation.home)

	implementation(projects.data)
	implementation(projects.overlay.main)
	implementation(projects.presentation.main)
	implementation(projects.core.alarm)
	implementation(projects.core.detection)

	implementation(libs.androidx.profileinstaller)

	testImplementation(projects.core.testing)
}
