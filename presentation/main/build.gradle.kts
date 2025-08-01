import com.yapp.breake.setNamespace

plugins {
	alias(libs.plugins.breake.android.feature)
}

android {
	setNamespace("presentation.main")

	defaultConfig {
		testInstrumentationRunner =
			"com.yapp.breake.core.testing.runner.BreakeTestRunner"
	}
}

dependencies {
	implementation(projects.presentation.login)
	implementation(projects.presentation.signup)
	implementation(projects.presentation.onboarding)
	implementation(projects.presentation.legal)
	implementation(projects.presentation.permission)
	implementation(projects.presentation.home)
	implementation(projects.presentation.report)
	implementation(projects.presentation.setting)
	implementation(projects.core.permission)

	androidTestImplementation(projects.core.testing)

	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.appcompat)
	implementation(libs.androidx.activity.compose)
	implementation(libs.androidx.lifecycle.runtimeCompose)
	implementation(libs.androidx.lifecycle.viewModelCompose)
	implementation(libs.kotlinx.immutable)
	androidTestImplementation(libs.hilt.android.testing)
	kspAndroidTest(libs.hilt.android.compiler)
}
