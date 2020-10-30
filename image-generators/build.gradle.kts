import java.io.File

plugins {
    kotlin("multiplatform") version "1.4.20-M2"
}

repositories {
    maven ("https://dl.bintray.com/kotlin/kotlin-eap")
    maven("https://dl.bintray.com/kotlin/kotlinx/")
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/kotlin/exposed/")
}

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
                api(project(":gabriela-image-api"))
            }
        }

        val jvmMain by getting {
            dependencies {
                api(project(":gabriela-image-api"))
            }
        }

        val jvmTest by getting {
            dependencies {
                api(project(":gabriela-image-api"))
                // api(project(":image-generators:ednaldo-bandeira"))

                // Required for tests, if this is missing then Gradle will throw
                // "No tests found for given includes: [***Test](filter.includeTestsMatching)"
                implementation(kotlin("test"))
                implementation(kotlin("test-junit"))
                implementation("org.junit.jupiter:junit-jupiter:5.4.2")
            }
        }
    }
}