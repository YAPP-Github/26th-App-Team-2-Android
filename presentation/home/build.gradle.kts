import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.home")
}

dependencies {
	implementation(projects.core.util)
	implementation(projects.core.appscanner)
}
