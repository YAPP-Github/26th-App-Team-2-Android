import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.compose)
}

android {
	setNamespace("core.designsystem")
}

dependencies {
	implementation(libs.androidx.appcompat)

	implementation(libs.landscapist.bom)
	implementation(libs.landscapist.coil)
	implementation(libs.landscapist.placeholder)

	implementation(libs.androidx.glance)
}
