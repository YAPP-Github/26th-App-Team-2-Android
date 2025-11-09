import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("overlay.timer")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.util)
	implementation(projects.overlay.ui)
	implementation(libs.snapper)
}
