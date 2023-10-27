dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
    api("org.postgresql:postgresql:42.6.0")

    api(project(":persistence"))
}
