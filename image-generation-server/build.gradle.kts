plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10" apply true
    id("com.google.cloud.tools.jib") version "3.1.4"
}

group = "net.perfectdreams.imagegeneratorserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":image-generators"))

    // Logging Stuff
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:1.8.3")

    api("io.ktor:ktor-server-core:1.4.1")
    api("io.ktor:ktor-server-netty:1.4.1")
    api("io.ktor:ktor-client-core:1.4.1")
    api("io.ktor:ktor-client-apache:1.4.1")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.0-M1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.0-M1")
    testImplementation("org.assertj:assertj-core:3.19.0")
}

tasks.test {
    useJUnitPlatform()
}

jib {
    extraDirectories {
        paths {
            path {
                setFrom("../assets")
                into = "/assets"
            }
        }
    }

    container {
        ports = listOf("8001")
        mainClass = "net.perfectdreams.imageserver.GabrielaImageGenLauncher"
    }

    to {
        image = "ghcr.io/lorittabot/gabriela-image-server:latest"

        auth {
            username = System.getProperty("DOCKER_USERNAME") ?: System.getenv("DOCKER_USERNAME")
            password = System.getProperty("DOCKER_PASSWORD") ?: System.getenv("DOCKER_PASSWORD")
        }
    }

    from {
        image = "ghcr.io/lorittabot/16-alpine-corretto-with-ffmpeg-gifsicle:main"
    }
}