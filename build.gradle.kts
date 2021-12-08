plugins {
    kotlin("multiplatform") version Versions.KOTLIN apply false
    kotlin("jvm") version Versions.KOTLIN apply false
    kotlin("plugin.serialization") version Versions.KOTLIN apply false
    id("maven-publish")
}

group = "net.perfectdreams.gabrielaimageserver"
version = Versions.GABRIELA_IMAGE_SERVER

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.perfectdreams.net/")
    }
}

subprojects {
    apply<MavenPublishPlugin>()
    group = "net.perfectdreams.gabrielaimageserver"
    version = Versions.GABRIELA_IMAGE_SERVER

    publishing {
        repositories {
            maven {
                name = "PerfectDreams"
                url = uri("https://repo.perfectdreams.net/")

                credentials {
                    username = System.getProperty("USERNAME") ?: System.getenv("USERNAME")
                    password = System.getProperty("PASSWORD") ?: System.getenv("PASSWORD")
                }
            }
        }
    }
}