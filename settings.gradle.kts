pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SkratchAssignment"
include(":app")
include(":core")
include(":core:designsystem")
include(":core:network")
include(":feature")
include(":feature:home")
include(":core:domain")
include(":core:data")
