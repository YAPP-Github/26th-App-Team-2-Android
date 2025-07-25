import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("presentation.permission")
}

dependencies {
	implementation(projects.core.permission)
}
