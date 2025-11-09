import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.hilt)
	alias(libs.plugins.brake.work.hilt)
}

android {
	setNamespace("core.alarm")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.common)
	implementation(projects.core.model)
	implementation(projects.core.util)
}
