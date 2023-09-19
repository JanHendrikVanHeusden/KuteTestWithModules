plugins {
    val kotlinJvmPluginVersion: String by System.getProperties()

    id("java")
    kotlin("jvm") version kotlinJvmPluginVersion
}

group = "nl.kute.kutetest"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    val dokkaVersion by System.getProperties()
    val junitPlatformVersion by System.getProperties()
    val jupiterVersion by System.getProperties()
    val assertJVersion by System.getProperties()
    val awaitilityVersion by System.getProperties()
    val commonsLangVersion by System.getProperties()

    // Kotlin
    compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")

    implementation("nl.kute:kute:1.0-SNAPSHOT")

    implementation("org.apache.commons:commons-lang3:$commonsLangVersion")

    // Used by Gradle tasks
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")

    // JUnit test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testImplementation("org.junit.platform:junit-platform-suite-api:$junitPlatformVersion")

    // Other test dependencies
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.awaitility:awaitility:$awaitilityVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}