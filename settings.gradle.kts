pluginManagement {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "GabrielaImageGen"

// Common module shared between the client and the image generators
include(":common")

// Gabriela Image Server's REST API Client
include(":client")

// Generators
include(":image-generators")

// Backend (Micro Service)
include(":image-generation-server")