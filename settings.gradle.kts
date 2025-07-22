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
	":presentation:home",
	":presentation:report",
	":presentation:setting",
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
	":core:datastore",
	":core:database",
	":core:model",
	":core:navigation",
	":core:permission",
	":core:designsystem",
	":core:util",
	":core:testing",
)
