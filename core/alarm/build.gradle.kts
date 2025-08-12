import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	alias(libs.plugins.breake.work.hilt)
}

android {
	setNamespace("core.alarm")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.common)
	implementation(projects.core.model)
	implementation(projects.core.util)
}
