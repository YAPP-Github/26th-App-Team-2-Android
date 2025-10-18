import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("overlay.blocking")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.util)

	implementation(projects.overlay.ui)
}
