import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.compose)
}

android {
	setNamespace("core.ui")
}

dependencies {
	implementation(projects.core.designsystem)
}
