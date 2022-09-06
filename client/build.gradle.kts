plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "net.perfectdreams.gabrielaimageserver"
version = libs.versions.gabrielaImageServer.get()

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
                api(project(":common"))

                implementation(libs.ktor.client.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
    }
}