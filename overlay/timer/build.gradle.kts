import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
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
