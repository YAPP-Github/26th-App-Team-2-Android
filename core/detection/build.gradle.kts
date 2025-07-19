import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
}

android {
	setNamespace("core.detection")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.utils)
	implementation(projects.core.common)
	implementation(projects.core.model)
}
