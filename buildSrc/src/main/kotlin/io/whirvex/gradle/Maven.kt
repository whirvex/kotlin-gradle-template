/*
 * Copyright Whirvex Software LLC, All rights reserved.
 */
package io.whirvex.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.repositories.PasswordCredentials
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.configure
import java.io.File
import java.net.URI

private val USER_GRADLE_DIR = File(
    System.getProperty("user.home"),
    ".gradle",
)

private val WHIRVEX_GRADLE_CONFIG_DIR = File(
    USER_GRADLE_DIR,
    "whirvex",
)

private val WHIRVEX_NEXUS_CONFIG_FILE = File(
    WHIRVEX_GRADLE_CONFIG_DIR,
    "nexus.config.json",
)

private val WHIRVEX_NEXUS_URI = URI(
    "https://nexus.whirvex.io/"
)

private val Any.isSnapshot: Boolean
    get() = this.toString().endsWith(
        suffix = "snapshot",
        ignoreCase = true,
    )

private fun PasswordCredentials.useWhirvexNexusCredentials() {
    val config = WHIRVEX_NEXUS_CONFIG_FILE.readNexusConfig()
    username = config.credentials.username
    password = config.credentials.password
}

/**
 * Adds the snapshot repository from the Whirvex Software central Nexus
 * repository for dependencies.
 *
 * **Example usage:**
 * ```kt
 * repositories {
 *     gradleCentral()
 *     whirvexNexusSnapshot()
 * }
 * ```
 *
 * @see whirvexNexus
 */
public fun RepositoryHandler.whirvexNexusSnapshot():
        MavenArtifactRepository = maven {
    this.name = "whirvexSnapshot"
    this.url = WHIRVEX_NEXUS_URI.resolve("repository/maven-snapshots")
    this.credentials { this.useWhirvexNexusCredentials() }
}

/**
 * Adds the releases repository from the Whirvex Software central Nexus
 * repository for dependencies.
 *
 * **Example usage:**
 * ```kt
 * repositories {
 *     gradleCentral()
 *     whirvexNexusRelease()
 * }
 * ```
 *
 * @see whirvexNexus
 */
public fun RepositoryHandler.whirvexNexusRelease():
        MavenArtifactRepository = maven {
    this.name = "whirvexRelease"
    this.url = WHIRVEX_NEXUS_URI.resolve("repository/maven-releases")
    this.credentials { this.useWhirvexNexusCredentials() }
}

/**
 * Adds both the snapshot and releases repository from the Whirvex Software
 * central Nexus repository for dependencies.
 *
 * **Example usage:**
 * ```kt
 * repositories {
 *     gradleCentral()
 *     whirvexNexus()
 * }
 * ```
 *
 * @see whirvexNexusSnapshot
 * @see whirvexNexusRelease
 */
public fun RepositoryHandler.whirvexNexus() {
    this.whirvexNexusSnapshot()
    this.whirvexNexusRelease()
}

/**
 * A plugin that automatically configures publishing to the
 * Whirvex Software central Nexus repository. For this plugin to work,
 * the project must have the `java-library` and `maven-publish` plugin
 * applied.
 *
 * **Example usage:**
 * ```kotlin
 * import io.whirvex.gradle.whirvexNexus
 *
 * plugins {
 *     id("java-library")
 *     id("maven-publish")
 *     id("whirvex-nexus")
 * }
 *
 * group = "io.whirvex"
 * version = "1.0.0"
 *
 * repositories {
 *     mavenCentral()
 *     whirvexNexus()
 * }
 * ```
 * Take note of the call to [whirvexNexus] in the `repositories` block.
 * This allows the project to use dependencies from the Whirvex Software
 * central Nexus repository. Without it, the project can be published to
 * the repository but cannot use any dependencies from it.
 */
public abstract class WhirvexNexusPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        project.extensions.configure<PublishingExtension> {
            publications {
                create("mavenJava", MavenPublication::class.java) {
                    from(project.components.getByName("java"))
                }
            }

            repositories {
                if (project.version.isSnapshot) {
                    whirvexNexusSnapshot()
                } else {
                    whirvexNexusRelease()
                }
            }
        }
    }

}
