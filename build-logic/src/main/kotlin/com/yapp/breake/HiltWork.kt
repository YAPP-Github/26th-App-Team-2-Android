package com.yapp.breake

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureHiltWork() {
	with(pluginManager) {
		apply("dagger.hilt.android.plugin")
		apply("com.google.devtools.ksp")
	}

	val libs = extensions.libs
	dependencies {
		"implementation"(libs.findLibrary("work.runtime.ktx").get())
		"implementation"(libs.findLibrary("work.hilt").get())
		"ksp"(libs.findLibrary("androidx.hilt.compiler").get())
	}
}

internal class HiltWorkPlugin : Plugin<Project> {

	override fun apply(target: Project) {
		with(target) {
			configureHiltWork()
		}
	}
}
