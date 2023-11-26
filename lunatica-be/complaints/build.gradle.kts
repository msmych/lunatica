plugins {
    id("io.ktor.plugin") version "2.3.5"
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-html-builder")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("com.github.pengrad:java-telegram-bot-api:6.9.1")
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("com.auth0:java-jwt:4.4.0")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.595")

    api(project(":persistence-pg"))
}
