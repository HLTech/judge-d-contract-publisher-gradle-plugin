import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    groovy
}

group = "com.hltech"
version = "0.1.0-SNAPSHOT"

apply(plugin = "maven")

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation(localGroovy())
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}