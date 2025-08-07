package com.yapp.breake

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureFirebase() {
	val libs = extensions.libs
	dependencies {
		"implementation"(platform(libs.findLibrary("firebase.bom").get()))
		"implementation"(libs.findLibrary("firebase.analytics").get())
		"implementation"(libs.findLibrary("firebase.crashlytics").get())
	}
}
