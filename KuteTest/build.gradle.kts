import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toUpperCaseAsciiOnly

group = "nl.kute.kutetest"
version = "1.0-SNAPSHOT"
description = "Tests of Kute from a Java application with Java Jigsaw module system"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    println("Running on JVM version: ${JavaVersion.current()}")
}

sourceSets.main {
    java.srcDirs("src/main/java")
}
sourceSets.test {
    java.srcDirs("src/test/java")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        // * the jsr-305=strict setting enforces strict nullability checks
        // * the jvm-default-all argument lets Java classes recognise default methods in Kotlin interfaces
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=all")
        jvmTarget = java.targetCompatibility.toString()
    }
}

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

plugins {
    // Values retrieved from gradle.properties
    // This seems about the only way; you cannot retrieve them otherwise from outside the plugins' scope
    val kotlinJvmPluginVersion: String by System.getProperties()
    val owaspDependencyCheckVersion: String by System.getProperties()
    val dependencyCheckVersion: String by System.getProperties()
    val dokkaVersion: String by System.getProperties()

    kotlin("jvm") version kotlinJvmPluginVersion

    `java-library`
    `maven-publish`
    idea

    id("java")
    id("org.owasp.dependencycheck") version owaspDependencyCheckVersion
    id("com.github.ben-manes.versions") version dependencyCheckVersion

    id("org.jetbrains.dokka") version dokkaVersion

}

dependencies {
    val dokkaVersion by System.getProperties()
    val junitPlatformVersion by System.getProperties()
    val jupiterVersion by System.getProperties()
    val mockitoKotlinVersion by System.getProperties()
    val assertJVersion by System.getProperties()
    val awaitilityVersion by System.getProperties()
    val commonsLangVersion by System.getProperties()

    implementation("nl.kute:kute:1.0-SNAPSHOT")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.apache.commons:commons-lang3:$commonsLangVersion")

    // Used by Gradle tasks
    compileOnly("org.jetbrains.dokka:dokka-gradle-plugin:$dokkaVersion")

    // Test stuff that inherit version from Kotlin version
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    // JUnit test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jupiterVersion")
    testImplementation("org.junit.platform:junit-platform-suite-api:$junitPlatformVersion")

    // Other test dependencies
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.awaitility:awaitility:$awaitilityVersion")

}

dependencyCheck {
    formats = listOf("json", "html")
    outputDirectory = "build/reports/owasp"
    failBuildOnCVSS = 8.0F
    failOnError = false
    suppressionFile = "owasp-suppression.xml"
    println("OWASP dependency reports:")
    formats.forEach {
        println("\t$outputDirectory/dependency-check-report.$it")
    }
    println("Suppression file: $suppressionFile")
}

tasks.withType<DependencyUpdatesTask> {
    // In Gradle widget, this task can be found under Tasks -> help
    // (do not confuse with tasks under `owasp dependency-check`
    outputDir = "build/reports/dependencies"
    reportfileName = "dependencies-report" // .txt will be added implicitly
    rejectVersionIf {
        isVersionNonStable(this.candidate.version) && !isVersionNonStable(this.currentVersion)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

fun isVersionNonStable(version: String): Boolean {
    val hasStableKeyword = listOf("RELEASE", "FINAL", "GA")
        .any { version.toUpperCaseAsciiOnly().contains(it) }
    val stableVersionPattern = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = hasStableKeyword || stableVersionPattern.matches(version)
    return isStable.not()
}
