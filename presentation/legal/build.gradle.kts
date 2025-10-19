import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.feature)
}

android {
	setNamespace("presentation.legal")
}

dependencies {
	implementation(libs.androidx.browser)
}
