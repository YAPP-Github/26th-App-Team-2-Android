import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.compose)
	alias(libs.plugins.kotlin.serialization)
}

android {
	setNamespace("core.navigation")
}

dependencies {
	implementation(libs.kotlinx.serialization.json)
	api(libs.androidx.navigation3.runtime)
	api(libs.androidx.navigation3.ui)
}
