import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.library)
	alias(libs.plugins.breake.android.hilt)
	alias(libs.plugins.kotlin.serialization)
}

android {
	setNamespace("data")

	buildFeatures {
		buildConfig = true
	}
}

dependencies {
	implementation(projects.domain)
	implementation(projects.core.auth)
	implementation(projects.core.model)
	implementation(projects.core.util)
	implementation(projects.core.datastore)
	implementation(projects.core.database)
	implementation(projects.core.common)
	implementation(projects.core.appscanner)
	implementation(projects.domain)

	implementation(libs.datastore)
	implementation(libs.retrofit.core)
	implementation(libs.retrofit.kotlin.serialization)
	implementation(libs.okhttp.logging)
	implementation(libs.kotlinx.serialization.json)
	implementation(libs.kotlinx.datetime)
	implementation(libs.sandwich.retrofit)
	testImplementation(libs.turbine)
}
