plugins {
    kotlin("multiplatform") version Versions.KOTLIN
    kotlin("plugin.serialization") version Versions.KOTLIN
    `maven-publish`
}

group = "net.perfectdreams.gabrielaimageserver"
version = Versions.GABRIELA_IMAGE_SERVER

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }

        withJava()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":common"))

                api("io.ktor:ktor-client-core:${Versions.KTOR}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}")
            }
        }
    }
}

publishing {
    publications {
        register("PerfectDreams", MavenPublication::class.java) {
            from(components["java"])
        }
    }
}