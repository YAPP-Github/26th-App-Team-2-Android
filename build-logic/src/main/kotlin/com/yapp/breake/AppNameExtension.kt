package com.yapp.breake

import org.gradle.api.Project

fun Project.setNamespace(name: String) {
    androidExtension.apply {
        namespace = "com.yapp.breake.$name"
    }
}