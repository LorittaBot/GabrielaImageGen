plugins {
    kotlin("multiplatform") version "1.4.20-M2"
}

repositories {
    mavenCentral()
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

group = "net.perfectdreams.imagegeneratorbrowser"
version = "1.0-SNAPSHOT"

kotlin {
    js {
        browser {
            binaries.executable()
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":image-generators"))
                implementation(project(":image-generation-browser:generators-info"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.2")
                implementation("io.ktor:ktor-client-js:1.4.1")
            }
        }
    }
}