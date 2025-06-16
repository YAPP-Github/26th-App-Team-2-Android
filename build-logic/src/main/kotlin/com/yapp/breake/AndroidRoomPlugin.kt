package com.yapp.breake

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidRoom() {
    with(pluginManager) {
        apply("com.google.devtools.ksp")
    }

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("room-runtime").get())
        "implementation"(libs.findLibrary("room-ktx").get())
        "ksp"(libs.findLibrary("room-compiler").get())
    }
}

class AndroidRoomPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureAndroidRoom()
        }
    }
}