plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "net.perfectdreams.gabrielaimageserver"
version = libs.versions.gabrielaImageServer.get()

kotlin {
    jvm {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_25)
        }

    }

    js {
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