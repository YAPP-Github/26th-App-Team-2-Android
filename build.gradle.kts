@file:Suppress("DSL_SCOPE_VIOLATION")

buildscript {
	repositories {
		google()
		mavenCentral()
	}
	dependencies {
		classpath(libs.oss.licenses.plugin)
	}
}

plugins {
	alias(libs.plugins.android.application) apply false
	alias(libs.plugins.android.library) apply false
	alias(libs.plugins.kotlin.android) apply false
	alias(libs.plugins.kotlin.jvm) apply false
	alias(libs.plugins.kotlin.serialization) apply false
	alias(libs.plugins.ksp) apply false
	alias(libs.plugins.hilt) apply false
	alias(libs.plugins.room) apply false
	alias(libs.plugins.ktlint) apply false
	alias(libs.plugins.verify.detekt) apply false
	alias(libs.plugins.compose.compiler) apply false
	alias(libs.plugins.android.test) apply false
	alias(libs.plugins.baselineprofile) apply false
	alias(libs.plugins.roborazzi.plugin) apply false
}

allprojects {
	apply {
		plugin(rootProject.libs.plugins.ktlint.get().pluginId)
	}
}

apply {
	from("gradle/dependencyGraph.gradle")
}
