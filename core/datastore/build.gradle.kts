import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.kotlin.serialization)
}

android {
	setNamespace("core.datastore")
}

dependencies {
	implementation(projects.core.model)

	implementation(libs.datastore)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinx.immutable)

	testImplementation(libs.junit4)
	testImplementation(libs.kotlin.test)
}
