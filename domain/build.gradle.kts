plugins {
	alias(libs.plugins.breake.kotlin.library)
}

dependencies {
	implementation(projects.core.model)
	implementation(libs.inject)
}
