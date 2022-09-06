plugins {
    kotlin("multiplatform") version libs.versions.kotlin apply false
    kotlin("jvm") version libs.versions.kotlin apply false
    kotlin("plugin.serialization") version libs.versions.kotlin apply false
    id("maven-publish")
}

// Needs to do this because trying to access "libs" within the subprojects block doesn't work
val GABRIELA_VERSION = libs.versions.gabrielaImageServer
group = "net.perfectdreams.gabrielaimageserver"
version = GABRIELA_VERSION

repositories {
    mavenCentral()
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.perfectdreams.net/")
    }
}

subprojects {
    apply<MavenPublishPlugin>()
    group = "net.perfectdreams.gabrielaimageserver"
    version = GABRIELA_VERSION

    publishing {
        repositories {
            maven {
                name = "PerfectDreams"
                url = uri("https://repo.perfectdreams.net/")
                credentials(PasswordCredentials::class)
            }
        }
    }
}