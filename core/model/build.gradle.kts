plugins {
	alias(libs.plugins.breake.kotlin.library)
	alias(libs.plugins.kotlin.serialization)
}

dependencies {
	api(libs.kotlinx.datetime)
	implementation(libs.kotlinx.serialization.json)
}
