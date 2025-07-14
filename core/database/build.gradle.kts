import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	alias(libs.plugins.breake.android.room)
}

android {
	setNamespace("core.database")

	defaultConfig {
		ksp {
			arg("room.schemaLocation", "$projectDir/schemas")
		}
	}
}

dependencies {
	implementation(projects.core.model)

	implementation(libs.junit4)
	implementation(libs.androidx.test.ext)
	implementation(libs.hilt.android.testing)
	implementation(libs.coroutines.test)
}
