import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.onboarding")
}

dependencies {
	implementation(projects.core.permission)
}
