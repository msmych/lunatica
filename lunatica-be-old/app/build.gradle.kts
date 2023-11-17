plugins {
    id("io.ktor.plugin") version "2.3.5"
    id("org.flywaydb.flyway") version "9.22.3"
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-call-logging")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")
    implementation("com.github.pengrad:java-telegram-bot-api:6.9.1")
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    implementation("ch.qos.logback:logback-classic:1.2.9")

    implementation(project(":complaints"))
}

flyway {
    url = "jdbc:postgresql://localhost:55000/postgres"
    user = "postgres"
    password = "postgres"
    driver = "org.postgresql.Driver"
    locations = arrayOf("filesystem:src/main/resources/db/migrations")
}
