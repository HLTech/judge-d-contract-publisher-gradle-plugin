package com.hltech.judged.contracts.publisher.gradle.plugin

import com.hltech.judged.contracts.publisher.capabilities.CapabilitiesReaderFactory
import com.hltech.judged.contracts.publisher.judged.JudgeD
import org.gradle.api.Plugin
import org.gradle.api.Project

class PublisherPlugin : Plugin<Project> {

    private val capabilitiesReaderFactory = CapabilitiesReaderFactory()
    private val judgeD = JudgeD()

    override fun apply(project: Project) {
        project.task("publishContracts") { task ->
            task.doLast {
                println("serviceName=${project.name}")
                println("serviceVersion=${project.version}")
                println("judgeDLocation=${project.properties["judgeDLocation"]}")
                println("capabilities=${project.properties["capabilities"]}")
                println("expectations=${project.properties["expectations"]}")

                val capabilities = getCapabilitiesFrom(project)
                    .map { capabilitiesReaderFactory.create(it).read(project) }
                    .toList()

                if (capabilities.isEmpty()) {
                    println("No capabilities to publish")
                } else {
                    judgeD.publishContracts(project, capabilities)
                }
            }
        }
    }

    private fun getCapabilitiesFrom(project: Project): Set<String> =
        splitProperty(project, CAPABILITIES_KEY)

    private fun getExpectationsFrom(project: Project): Set<String> =
        splitProperty(project, EXPECTATIONS_KEY)

    private fun splitProperty(project: Project, property: String): Set<String> =
        when (val capabilities = project.properties[property]) {
            is String -> capabilities.split(",")
                .map { it.trim() }
                .toSet()
            else -> emptySet()
        }

    private companion object {
        private const val CAPABILITIES_KEY = "capabilities"
        private const val EXPECTATIONS_KEY = "expectations"
    }
}