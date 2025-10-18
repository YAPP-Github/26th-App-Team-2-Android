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
	implementation(libs.androidx.compose.navigation)
}
