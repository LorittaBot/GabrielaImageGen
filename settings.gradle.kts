pluginManagement {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "GabrielaImageGen"

// Image API
include(":gabriela-image-api")

// Generators
include(":image-generators")

// Browser Version
include(":image-generation-browser:generators-info")
include(":image-generation-browser:backend")
include(":image-generation-browser:frontend")

// Backend (Micro Service)
include(":image-generation-server")