import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.compose)
	alias(libs.plugins.kotlin.serialization)
}

android {
	setNamespace("core.navigation")
}

dependencies {
	implementation(libs.kotlinx.serialization.json)
}
