import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
}

android {
	setNamespace("core.service")
}

dependencies {
	implementation(projects.core.database)
	implementation(projects.core.model)
	implementation(projects.presentation.overlay)
}
