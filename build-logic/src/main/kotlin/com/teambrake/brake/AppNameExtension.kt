package com.teambrake.brake

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.teambrake.brake.$name"
    }
}
