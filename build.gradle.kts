plugins {
    kotlin("jvm") version "1.6.20"
}

group = "app.revanced"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/ReVancedTeam/revanced-patcher") // note the "r"!
        credentials {
            // DO NOT set these variables in the project's gradle.properties.
            // Instead, you should set them in:
            // Windows: %homepath%\.gradle\gradle.properties
            // Linux: ~/.gradle/gradle.properties
            username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR") // DO NOT CHANGE!
            password = project.findProperty("gpr.key")  as String? ?: System.getenv("GITHUB_TOKEN") // DO NOT CHANGE!
        }
    }
}

dependencies {
    implementation("app.revanced:multidexlib2:2.5.2")
    implementation("org.smali:dexlib2:2.5.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation(kotlin("stdlib"))
}