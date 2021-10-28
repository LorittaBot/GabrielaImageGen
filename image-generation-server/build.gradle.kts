plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.cloud.tools.jib") version Versions.JIB
}

group = "net.perfectdreams.imagegeneratorserver"
version = Versions.GABRIELA_IMAGE_SERVER

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":common"))
    api(project(":image-generators"))

    // Logging Stuff
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    api("io.ktor:ktor-server-core:${Versions.KTOR}")
    api("io.ktor:ktor-server-netty:${Versions.KTOR}")
    api("io.ktor:ktor-client-core:${Versions.KTOR}")
    api("io.ktor:ktor-client-apache:${Versions.KTOR}")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}")
    api("org.jetbrains.kotlinx:kotlinx-serialization-hocon:${Versions.KOTLINX_SERIALIZATION}")
    
    // Prometheus, for metrics
    api("io.prometheus:simpleclient:${Versions.PROMETHEUS}")
    api("io.prometheus:simpleclient_hotspot:${Versions.PROMETHEUS}")
    api("io.prometheus:simpleclient_common:${Versions.PROMETHEUS}")

    // Required for tests, if this is missing then Gradle will throw
    // "No tests found for given includes: [***Test](filter.includeTestsMatching)"
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.assertj:assertj-core:3.19.0")

    testImplementation(project(":client"))
    testImplementation("io.ktor:ktor-client-apache:1.6.3")
    testImplementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    testImplementation(kotlin("reflect"))
}

tasks {
    processResources {
        from("../assets/") // Include folders from the resources root folder
    }

    test {
        dependsOn(jibDockerBuild)
        useJUnitPlatform()
    }
}

jib {
    extraDirectories {
        paths {
            path {
                setFrom("src/main/jib/")
                into = "/"
            }

            path {
                setFrom("../assets")
                into = "/assets"
            }
        }
    }

    container {
        ports = listOf("8001")
        mainClass = "net.perfectdreams.gabrielaimageserver.webserver.GabrielaImageGenLauncher"
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