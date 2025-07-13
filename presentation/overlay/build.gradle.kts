import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
	id("kotlin-parcelize")
}

android {
	setNamespace("presentation.overlay")
}

dependencies {
	implementation(projects.core.common)
	implementation(projects.core.alarm)

	implementation(libs.accompanist.permissions)
	implementation(libs.androidx.lifecycle.runtimeCompose)

}
