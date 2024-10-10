import java.io.FileInputStream
import java.util.Properties


pluginManagement {
    repositories {
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
val prop = Properties().apply {
    load(FileInputStream(File(rootProject.projectDir, "local.properties")))
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            setUrl(prop.getProperty("GITHUB_URL"))
            credentials {
                username = prop.getProperty("GITHUB_USERNAME")
                password = prop.getProperty("GITHUB_TOKEN")
            }
        }
    }
}

rootProject.name = "My Application"
include(":app")
include(":library")
