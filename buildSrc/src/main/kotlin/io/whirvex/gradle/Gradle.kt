/*
 * Copyright Whirvex Software LLC, All rights reserved.
 */
import org.gradle.api.Project

private const val WHIRVEX_NEXUS_PROPERTIES_PATH =
    "io.whirvex.gradle.whirvexNexus"

private val EXEMPT_PROJECT_PROPERTY_PATH =
    WHIRVEX_NEXUS_PROPERTIES_PATH + ".exemptProject"

/**
 * Returns if this project should not have the `whirvex-nexus`
 * plugin applied.
 *
 * This property defaults to `false` when not specified.
 *
 * **Example usage:**
 * ```properties
 * # file: gradle.properties
 * io.whirvex.gradle.whirvexNexus.exemptProject=true
 * ```
 */
public val Project.isExemptFromWhirvexNexus: Boolean
    get() = this
        .findProperty(EXEMPT_PROJECT_PROPERTY_PATH)
        ?.let { it.toString().toBoolean() }
        ?: false
