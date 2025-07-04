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

subprojects {
	// 모든 subproject에 대해 kotlin 플러그인 적용
	apply(plugin = rootProject.libs.plugins.ktlint.get().pluginId)

	// ./gradlew build : 모든 kt 파일에 대해 ktlintFormat 실행
	// android :app build : 어노테이션 미적용 kt 파일에 대해 ktlintFormat 실행
	tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
		dependsOn("ktlintFormat")
	}
}

apply {
	from("gradle/dependencyGraph.gradle")
}
