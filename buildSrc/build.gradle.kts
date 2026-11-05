plugins {
    `kotlin-dsl`
}

kotlin {
    explicitApi()
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.google.code.gson:gson:2.13.2")
}

gradlePlugin {
    plugins {
        create("whirvex-nexus") {
            id = "whirvex-nexus"
            implementationClass = "io.whirvex.gradle.WhirvexNexusPlugin"
        }
    }
}
