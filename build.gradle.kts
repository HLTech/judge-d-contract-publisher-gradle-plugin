import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    groovy
    maven
    idea
    `java-gradle-plugin`
    id("com.palantir.idea-test-fix") version ("0.1.0")
}

group = "com.hltech"
version = "0.1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(gradleApi())
    implementation(localGroovy())
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.+")
    implementation("io.github.openfeign:feign-core:9.7.0")
    implementation("io.github.openfeign:feign-jackson:9.7.0")

    testImplementation(gradleTestKit())
    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")
    testImplementation("com.github.tomakehurst:wiremock:2.13.0")
}
