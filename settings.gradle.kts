pluginManagement {
    repositories {
        maven("https://dl.bintray.com/kotlin/kotlin-eap")

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
include(":image-generator-browser:generators-info")
include(":image-generator-browser:backend")
include(":image-generator-browser:frontend")

// Backend
include(":image-generator-server")