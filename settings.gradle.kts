pluginManagement {
	includeBuild("build-logic")
	repositories {
		google()
		mavenCentral()
		gradlePluginPortal()
	}
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
		maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
	}
}

rootProject.name = "Breake"

// Application
include(":app")

// Presentation
include(
	":presentation:main",
	":presentation:signup",
	":presentation:login",
	":presentation:onboarding",
	":presentation:permission",
	":presentation:home",
	":presentation:report",
	":presentation:setting",
)

// overlay
include(
	"overlay:main",
	"overlay:ui",
	"overlay:timer",
	"overlay:snooze",
	"overlay:blocking",
)

// Domain
include(":domain")

// Data
include(":data")

// Data Test
include(":data-test")

// Core
include(
	":core:auth",
	":core:alarm",
	":core:common",
	":core:datastore",
	":core:database",
	":core:designsystem",
	":core:model",
	":core:navigation",
	":core:permission",
	":core:detection",
	":core:service",
	":core:testing",
	":core:util",
)
