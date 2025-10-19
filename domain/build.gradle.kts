plugins {
	alias(libs.plugins.brake.kotlin.library)
}

dependencies {
	implementation(projects.core.model)
	implementation(projects.core.common)

	implementation(libs.inject)
}
