plugins {
    `maven-publish`
}

group = "net.perfectdreams.gabrielaimageserver"
version = Versions.GABRIELA_IMAGE_SERVER

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply<MavenPublishPlugin>()
    version = "0.0.10-SNAPSHOT"

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