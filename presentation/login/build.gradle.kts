import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("presentation.login")
}

dependencies {
	implementation(projects.core.auth)
	implementation(projects.core.permission)

	implementation(libs.google.auth)
}
