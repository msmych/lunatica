import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.1.5"
    kotlin("plugin.spring") version "1.8.0"
}

apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation("com.neovisionaries:nv-i18n:1.29")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.github.pengrad:java-telegram-bot-api:6.9.1")
    implementation("com.google.firebase:firebase-admin:9.2.0") {
        exclude(group = "org.slf4j")
    }
}

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
    }
}

kotlin {
    jvmToolchain(17)
}
