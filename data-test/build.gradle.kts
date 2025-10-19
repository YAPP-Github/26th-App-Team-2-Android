import com.teambrake.brake.setNamespace

plugins {
	alias(libs.plugins.brake.android.library)
	alias(libs.plugins.brake.android.hilt)
	alias(libs.plugins.kotlin.serialization)
}

android {
	setNamespace("dataTest")
}

dependencies {
	implementation(projects.core.model)
	implementation(projects.core.datastore)
	implementation(projects.core.database)
	implementation(projects.domain)

	implementation(libs.datastore)
	implementation(libs.retrofit.core)
	implementation(libs.retrofit.kotlin.serialization)
	implementation(libs.okhttp.logging)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinx.datetime)
	testImplementation(libs.turbine)
}
