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
	}
}

rootProject.name = "Breake"

// Application
include(":app")

// Presentation
include(
	":presentation:main",
	":presentation:login",
	":presentation:home"
)

// Domain
include(":domain")

// Data
include(":data")

// Core
include(
	":core:datastore",
	":core:database",
	":core:model",
	":core:navigation",
	":core:designsystem",
	":core:testing"
)
