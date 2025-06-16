plugins {
	alias(libs.plugins.breake.android.application)
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
}

dependencies {

}