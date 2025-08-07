import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
}

android {
	setNamespace("core.detection")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.util)
	implementation(projects.core.common)
	implementation(projects.core.model)

	implementation(platform(libs.firebase.bom))
	implementation(libs.firebase.analytics)
	implementation(libs.firebase.crashlytics)
}
