plugins {
    kotlin("jvm")
}

group = "net.perfectdreams.gabrielaimageserver"
version = libs.versions.gabrielaImageServer.get()

dependencies {
    api(project(":common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinLogging)

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