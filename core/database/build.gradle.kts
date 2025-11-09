import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.hilt)
	alias(libs.plugins.brake.android.room)
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
	implementation(libs.kotlinx.serialization.json)
}
