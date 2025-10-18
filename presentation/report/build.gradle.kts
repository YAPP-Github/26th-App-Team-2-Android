import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.report")
}

dependencies {
	implementation(projects.core.util)
}
