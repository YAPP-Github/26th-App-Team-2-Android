import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
}

android {
	setNamespace("core.testing")
}

dependencies {
	implementation(libs.inject)
}
