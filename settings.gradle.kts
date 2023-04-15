pluginManagement {
    repositories {
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
}

rootProject.name = "GabrielaImageGen"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val gabrielaImageServer = version("gabrielaImageServer", "2.0.14-SNAPSHOT")
            val kotlin = version("kotlin", "1.7.10")
            val kotlinXSerialization = version("kotlinx-serialization", "1.4.0-RC")
            val ktor = version("ktor", "2.2.4")
            val jib = version("jib", "3.3.0")
            val logback = version("logback", "1.3.0-alpha16")
            val kotlinxCoroutines = version("kotlinx-coroutines", "1.6.4")
            val prometheus = version("prometheus", "0.16.0")

            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef(kotlinxCoroutines)
            library("kotlinx-coroutines-debug", "org.jetbrains.kotlinx", "kotlinx-coroutines-debug").versionRef(kotlinxCoroutines)

            library("kotlinLogging", "io.github.microutils", "kotlin-logging").version("2.1.23")

            library("kotlinx-serialization-core", "org.jetbrains.kotlinx", "kotlinx-serialization-core").versionRef(kotlinXSerialization)
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx", "kotlinx-serialization-json").versionRef(kotlinXSerialization)
            library("kotlinx-serialization-protobuf", "org.jetbrains.kotlinx", "kotlinx-serialization-protobuf").versionRef(kotlinXSerialization)
            library("kotlinx-serialization-hocon", "org.jetbrains.kotlinx", "kotlinx-serialization-hocon").versionRef(kotlinXSerialization)
            library("ktor-server-cio", "io.ktor", "ktor-server-cio").versionRef(ktor)
            library("ktor-server-compression", "io.ktor", "ktor-server-compression").versionRef(ktor)
            library("ktor-server-statusPages", "io.ktor", "ktor-server-status-pages").versionRef(ktor)
            library("ktor-client-core", "io.ktor", "ktor-client-core").versionRef(ktor)
            library("ktor-client-js", "io.ktor", "ktor-client-js").versionRef(ktor)
            library("ktor-client-cio", "io.ktor", "ktor-client-cio").versionRef(ktor)

            library("prometheus-simpleclient", "io.prometheus", "simpleclient").versionRef(prometheus)
            library("prometheus-simpleclientHotspot", "io.prometheus", "simpleclient_hotspot").versionRef(prometheus)
            library("prometheus-simpleclientCommon", "io.prometheus", "simpleclient_common").versionRef(prometheus)

            library("logback-classic", "ch.qos.logback", "logback-classic").versionRef(logback)

            library("hikaricp", "com.zaxxer", "HikariCP").version("5.0.1")
        }
    }
}

// Common module shared between the client and the image generators
include(":common")

// Gabriela Image Server's REST API Client
include(":client")

// Generators
include(":image-generators")

// Backend (Micro Service)
include(":image-generation-server")