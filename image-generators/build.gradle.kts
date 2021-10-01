plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    api(project(":common"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")

    // https://mvnrepository.com/artifact/io.github.microutils/kotlin-logging
    implementation("io.github.microutils:kotlin-logging:2.0.11")

    // Required for tests, if this is missing then Gradle will throw
    // "No tests found for given includes: [***Test](filter.includeTestsMatching)"
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
    implementation("org.junit.jupiter:junit-jupiter:5.4.2")
}

tasks {
    processResources {
        from("../assets/") // Include folders from the resources root folder
    }
}