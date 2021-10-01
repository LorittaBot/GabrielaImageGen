plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "net.perfectdreams.gabrielaimageserver"
version = Versions.GABRIELA_IMAGE_SERVER

repositories {
    mavenCentral()
}

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

                api("io.ktor:ktor-client-core:${Versions.KTOR}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}")
            }
        }
    }
}