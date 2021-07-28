plugins {
    kotlin("multiplatform") version "1.4.20-M2"
}

repositories {
    mavenCentral()
    jcenter()
}

group = "net.perfectdreams.imagegeneratorbrowser"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }

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
            }
        }
    }
}