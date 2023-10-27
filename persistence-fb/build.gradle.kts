dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("com.google.firebase:firebase-admin:9.2.0") {
        exclude(group = "org.slf4j")
    }

    api(project(":persistence"))
}
