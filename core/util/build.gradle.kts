import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.compose)
	id("kotlin-parcelize")
}

android {
	setNamespace("core.utils")
}
dependencies {
	implementation(projects.core.common)
	implementation(projects.core.model)
}
