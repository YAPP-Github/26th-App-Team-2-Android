import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.compose)
	id("kotlin-parcelize")
}

android {
	setNamespace("core.utils")
}
dependencies {
	implementation(projects.core.common)
	implementation(projects.core.model)
}
