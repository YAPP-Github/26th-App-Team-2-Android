import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	id("kotlin-parcelize")
}

android {
	setNamespace("core.utils")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.common)
	implementation(projects.core.model)
}
