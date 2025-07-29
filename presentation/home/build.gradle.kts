import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("presentation.home")
}

dependencies {
	implementation(projects.core.util)
	implementation(projects.core.appscanner)
}
