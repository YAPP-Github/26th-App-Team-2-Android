import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
}

android {
	setNamespace("domain")
}

dependencies {
	implementation(projects.core.model)

	implementation(libs.inject)
}
