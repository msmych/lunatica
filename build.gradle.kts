plugins {
    kotlin("plugin.serialization") version "1.9.10"
    kotlin("jvm") version "1.9.10"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

    dependencies {
        implementation("com.neovisionaries:nv-i18n:1.29")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    }

    repositories {
        mavenCentral()
    }
}
