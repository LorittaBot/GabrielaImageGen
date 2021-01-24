/* plugins {
    kotlin("multiplatform") version "1.4.10"
}

group = "net.perfectdreams.imageserver"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://dl.bintray.com/kotlin/kotlinx/")
    mavenCentral()
    jcenter()
    maven("https://dl.bintray.com/kotlin/ktor")
    maven("https://dl.bintray.com/kotlin/exposed/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":image-generators"))

    // Logging Stuff
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:1.8.3")

    api("io.ktor:ktor-server-core:1.4.1")
    api("io.ktor:ktor-server-netty:1.4.1")
    api("io.ktor:ktor-client-core:1.4.1")
    api("io.ktor:ktor-client-apache:1.4.1")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.0.0")
} */