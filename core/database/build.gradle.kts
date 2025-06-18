import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	alias(libs.plugins.breake.android.room)
}

android {
	setNamespace("core.database")
}

dependencies {
	implementation(libs.junit4)
	implementation(libs.androidx.test.ext)
	implementation(libs.hilt.android.testing)
	implementation(libs.coroutines.test)
	implementation(kotlin("reflect"))
}