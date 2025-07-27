import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	id("kotlin-parcelize")
}

android {
	setNamespace("core.utils")
}
dependencies {
	implementation(projects.core.common)
	implementation(projects.core.model)
}
