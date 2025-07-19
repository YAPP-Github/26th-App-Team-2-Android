import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("overlay.blocking")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.alarm)
	implementation(projects.core.util)

	implementation(projects.overlay.ui)
}
