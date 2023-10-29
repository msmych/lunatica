plugins {
    java
    id("org.springframework.boot") version "3.1.5"
}

apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.google.firebase:firebase-admin:9.2.0") {
        exclude(group = "org.slf4j")
    }
}

repositories {
    mavenCentral()
}
