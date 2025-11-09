import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.compose)
}

android {
	setNamespace("core.designsystem")
}

dependencies {
	implementation(projects.core.util)

	implementation(libs.androidx.appcompat)

	implementation(libs.landscapist.bom)
	implementation(libs.landscapist.coil)
	implementation(libs.landscapist.placeholder)

	implementation(libs.androidx.glance)
}
