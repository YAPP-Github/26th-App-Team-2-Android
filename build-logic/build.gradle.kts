plugins {
	`kotlin-dsl`
	`kotlin-dsl-precompiled-script-plugins`
}

dependencies {
	implementation(libs.android.gradlePlugin)
	implementation(libs.kotlin.gradlePlugin)
	implementation(libs.verify.detektPlugin)
	compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
	plugins {
		register("androidHilt") {
			id = "brake.android.hilt"
			implementationClass = "com.teambrake.brake.HiltAndroidPlugin"
		}
		register("workHilt") {
			id = "brake.work.hilt"
			implementationClass = "com.teambrake.brake.HiltWorkPlugin"
		}
		register("kotlinHilt") {
			id = "brake.kotlin.hilt"
			implementationClass = "com.teambrake.brake.HiltKotlinPlugin"
		}
		register("androidRoom") {
			id = "brake.android.room"
			implementationClass = "com.teambrake.brake.AndroidRoomPlugin"
		}
	}
}
