import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.setting")

	defaultConfig {
		val versionName = libs.versions.versionName.get()
		buildConfigField("String", "VERSION_NAME", "\"$versionName\"")
	}

	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	implementation(projects.core.auth)

	implementation(libs.androidx.browser)
}
