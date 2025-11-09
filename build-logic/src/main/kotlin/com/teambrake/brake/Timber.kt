package com.teambrake.brake

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureTimber() {
	val libs = extensions.libs
	dependencies {
		"implementation"(libs.findLibrary("timber").get())
	}
}
