plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10" apply true
}

group = "net.perfectdreams.imagegeneratorserver"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
    maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    api(project(":image-generators"))
    api(project(":image-generation-browser:generators-info"))

    // Logging Stuff
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
    implementation("io.github.microutils:kotlin-logging:1.8.3")

    api("io.ktor:ktor-server-core:1.4.1")
    api("io.ktor:ktor-server-netty:1.4.1")
    api("io.ktor:ktor-html-builder:1.4.0")
    api("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")

    api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0")
    api("org.jetbrains.kotlinx:kotlinx-serialization-hocon:1.0.0")
}

tasks {
    val fatJar = task("fatJar", type = Jar::class) {
        // We only want to execute this AFTER everything was built, not during the configuration phase
        // doLast {
        /* println("Building fat jar for ${project.name}...")

        archiveBaseName.set("${project.name}-fat")

        manifest {
            attributes["Main-Class"] = "net.perfectdreams.imageserver.GabrielaImageGenLauncher"
            attributes["Class-Path"] =
                configurations.runtimeClasspath.get().joinToString(" ", transform = { "libs/" + it.name })
        }

        val libs = File(rootProject.projectDir, "libs")
        // libs.deleteRecursively()
        libs.mkdirs()

        from(configurations.runtimeClasspath.get().mapNotNull {
            val output = File(libs, it.name)

            if (!output.exists())
                it.copyTo(output, true)

            null
        })

        with(jar.get() as CopySpec) */
        // }
    }

    "build" {
        dependsOn(fatJar)
    }
}