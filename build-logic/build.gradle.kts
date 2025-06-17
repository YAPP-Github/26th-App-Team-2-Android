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
			id = "breake.android.hilt"
			implementationClass = "com.yapp.breake.HiltAndroidPlugin"
		}
		register("kotlinHilt") {
			id = "breake.kotlin.hilt"
			implementationClass = "com.yapp.breake.HiltKotlinPlugin"
		}
		register("androidRoom") {
			id = "breake.android.room"
			implementationClass = "com.yapp.breake.AndroidRoomPlugin"
		}
	}
}