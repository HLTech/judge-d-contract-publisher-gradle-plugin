import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
    groovy
    maven
    idea
    `java-gradle-plugin`
    id("com.palantir.idea-test-fix") version ("0.1.0")
    id("com.gradle.plugin-publish") version ("0.10.0")
}

group = "com.hltech"
version = "1.0.1"

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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.3.21")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.7")
    implementation("io.github.openfeign:feign-core:9.7.0")
    implementation("io.github.openfeign:feign-jackson:9.7.0")
    implementation("com.hltech:vaunt-core:1.0.16")

    testImplementation(gradleTestKit())
    testImplementation("org.spockframework:spock-core:1.2-groovy-2.5")
    testImplementation("com.github.tomakehurst:wiremock:2.13.0")
}

pluginBundle {
    website = "https://github.com/HLTech/judge-d-contract-publisher-gradle-plugin"
    vcsUrl = "https://github.com/HLTech/judge-d-contract-publisher-gradle-plugin.git"
    tags = listOf("contract-testing", "judge-dredd", "pact-gen", "swagger", "hltech")
}

gradlePlugin {
    plugins {
        create("contractsPublisher") {
            id = "com.hltech.judged.contracts.publisher"
            displayName = "Plugin for publishing contracts to judge-dredd"
            description = "A plugin that helps you publish service expectations and capabilities to judge-dredd"
            implementationClass = "com.hltech.judged.contracts.publisher.gradle.plugin.PublisherPlugin"
        }
    }
}