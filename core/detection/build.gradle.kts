import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.hilt)
}

android {
	setNamespace("core.detection")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.util)
	implementation(projects.core.common)
	implementation(projects.core.model)
}
