import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
}

android {
	setNamespace("core.alarm")
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.model)
	implementation(projects.core.common)
}
