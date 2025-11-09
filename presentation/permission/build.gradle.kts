import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.permission")
}

dependencies {
	implementation(projects.core.permission)
}
