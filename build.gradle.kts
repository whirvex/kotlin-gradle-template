plugins {
    kotlin("jvm") version "2.4.21"
    id("org.jetbrains.dokka") version "2.1.0"
    `java-library`
    `maven-publish`
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

subprojects {
    plugins.apply("kotlin")

    if (!project.isExemptFromWhirvexNexus) {
        plugins.apply("java-library")
        plugins.apply("maven-publish")
        plugins.apply("whirvex-nexus")
    }

    group = "io.whirvex"

    kotlin {
        jvmToolchain(jdkVersion = 21)
        explicitApi()
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

        testImplementation(kotlin("test"))
    }

    tasks.test {
        useJUnitPlatform()
    }
}
