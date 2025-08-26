import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
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
