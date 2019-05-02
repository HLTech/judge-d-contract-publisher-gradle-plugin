package com.hltech.judged.contracts.publisher.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class PublisherPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("publishContracts", PublisherPluginExtension::class.java)
        project.task("publishContracts") {
            it.doLast {
                println("${extension.message} from ${extension.greeter}")
            }
        }
    }
}