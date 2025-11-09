import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.login")
}

dependencies {
	implementation(projects.core.auth)
	implementation(projects.core.permission)

	implementation(libs.google.auth)
}
