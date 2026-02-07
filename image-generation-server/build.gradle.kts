plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("com.google.cloud.tools.jib") version libs.versions.jib
}

group = "net.perfectdreams.imagegeneratorserver"
version = libs.versions.gabrielaImageServer.get()

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":common"))
    api(project(":image-generators"))

    // Logging Stuff
    implementation(libs.logback.classic)
    implementation(libs.kotlinLogging)

    implementation(libs.ktor.server.cio)
    implementation(libs.ktor.server.compression)
    implementation(libs.ktor.server.statusPages)
    implementation(libs.ktor.client.cio)

    implementation("net.perfectdreams.sequins.ktor:base-route:1.0.4")

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.hocon)
    
    // Prometheus, for metrics
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.hocon)

    implementation(libs.prometheus.simpleclient)
    implementation(libs.prometheus.simpleclientHotspot)
    implementation(libs.prometheus.simpleclientCommon)

    implementation("net.perfectdreams.libwebpffm:libwebp-ffm:0.0.1")

    // Required for tests, if this is missing then Gradle will throw
    // "No tests found for given includes: [***Test](filter.includeTestsMatching)"
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("org.assertj:assertj-core:3.19.0")

    testImplementation(project(":client"))
    testImplementation("io.ktor:ktor-client-apache:1.6.3")
    testImplementation("ch.qos.logback:logback-classic:1.3.0-alpha14")
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
        // This image comes from the "docker" folder Dockerfile!
        // Don't forget to build the image before compiling!
        // https://github.com/GoogleContainerTools/jib/issues/1468
        image = "tar://${File(rootDir, "docker/image.tar").absoluteFile}"
    }
}