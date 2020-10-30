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

include(":image-generator-server")
include(":image-generator-browser")