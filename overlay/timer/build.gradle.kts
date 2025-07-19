import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("overlay.timer")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.alarm)
}
