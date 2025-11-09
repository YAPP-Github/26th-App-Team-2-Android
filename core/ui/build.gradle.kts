import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.compose)
}

android {
	setNamespace("core.ui")
}

dependencies {
	implementation(projects.core.designsystem)
}
