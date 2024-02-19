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

        // Mapbox repo setup
        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            credentials.username = "mapbox"

            // This should be defined in local.properties, but I will leave it here to make sure app
            // works when testing it
            credentials.password =
                "sk.eyJ1IjoidGVvdmxhZHVzaWMiLCJhIjoiY2xzbmJnZTNpMDJ0MTJsazU0dXY5ODV2ZiJ9.QBzmCXwtI_SUwtu6Ujwgqg"

            authentication.create<BasicAuthentication>("basic")
        }
    }
}

rootProject.name = "SkratchAssignment"
include(":app")
include(":core")
include(":core:designsystem")
include(":feature")
include(":core:domain")
include(":core:data")
include(":core:ui")
include(":core:common")
include(":feature:friend_details")
include(":feature:friends_map_view")
include(":feature:friends_list_view")
