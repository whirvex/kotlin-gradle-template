plugins {
    kotlin("jvm") version "2.3.0"
    id("org.jetbrains.dokka") version "2.1.0"
    `java-library`
    `maven-publish`
    id("whirvex-nexus")
}

group = "io.whirvex"
version = "0.0.1-SNAPSHOT"

kotlin {
    jvmToolchain(jdkVersion = 21)
    explicitApi()
    compilerOptions.freeCompilerArgs.add("-Xcontext-parameters")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

    testImplementation(kotlin("test"))
}

dokka {
    moduleName.set(TODO())
    dokkaSourceSets.main {
        includes.from("src/Module.md")
    }
}

tasks.test {
    useJUnitPlatform()
}
