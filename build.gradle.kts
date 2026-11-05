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

/*
 * Subproject versions must be set in the root project. The "publish"
 * task does not see the version set by a subproject's respective build
 * file for some reason.
 */

// project(":foo") {
//     version = "0.0.1-SNAPSHOT"
// }

/*
 * Add this code to a project's build.gradle.kts to use Dokka.
 */

// // foo/build.gradle.kts
//
// plugins.apply("org.jetbrains.dokka")
//
// dokka {
//     moduleName.set("Foo")
//     dokkaSourceSets.main {
//         includes.from("src/Module.md")
//     }
// }

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

    dependencies {
        testImplementation(kotlin("test"))

        constraints {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.11.0")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}
