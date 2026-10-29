pluginManagement {
    repositories {
        // PICO Spatial SDK is published to this public artifact repository.
        maven { url = uri("https://artifact.bytedance.com/repository/Volcengine") }
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://artifact.bytedance.com/repository/Volcengine") }
        google()
        mavenCentral()
    }
}

rootProject.name = "Solar System"

include(":app")
