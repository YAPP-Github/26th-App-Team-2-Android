import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.hilt)
}

android {
	setNamespace("core.permission")
}
