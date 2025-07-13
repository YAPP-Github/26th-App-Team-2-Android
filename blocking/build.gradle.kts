import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
	id("kotlin-parcelize")
}

android {
	setNamespace("overlay")
}

dependencies {
	implementation(projects.core.common)

	implementation(libs.accompanist.permissions)
	implementation(libs.androidx.lifecycle.runtimeCompose)

}
