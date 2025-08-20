import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("presentation.report")
}

dependencies {
	implementation(projects.core.util)
}
