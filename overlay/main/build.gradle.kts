import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("overlay.main")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.alarm)
	implementation(projects.core.util)

	implementation(projects.overlay.timer)
	implementation(projects.overlay.snooze)
	implementation(projects.overlay.blocking)

	implementation(libs.accompanist.permissions)
	implementation(libs.androidx.lifecycle.runtimeCompose)

}
