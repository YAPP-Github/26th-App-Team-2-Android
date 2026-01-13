package com.teambrake.brake

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAmplitude() {
	val libs = extensions.libs
	dependencies {
		"implementation"(libs.findLibrary("amplitude.analytics").get())
	}
}
