plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = "net.perfectdreams.gabrielaimageserver"
version = Versions.GABRIELA_IMAGE_SERVER

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }

    js(IR) {
        // Declares that we want to compile for the browser and for nodejs
        browser()
        nodejs()
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
            }
        }
    }
}