import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("snooze")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.util)

	implementation(projects.overlay.ui)
}
