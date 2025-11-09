import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("overlay.main")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.util)

	implementation(projects.overlay.timer)
	implementation(projects.overlay.snooze)
	implementation(projects.overlay.blocking)

	implementation(libs.accompanist.permissions)
	implementation(libs.androidx.lifecycle.runtimeCompose)

}
