package com.hltech.judged.contracts.publisher.gradle.plugin

import com.hltech.judged.contracts.publisher.capabilities.Capabilities
import com.hltech.judged.contracts.publisher.capabilities.CapabilitiesReaderFactory
import com.hltech.judged.contracts.publisher.expectations.Expectations
import com.hltech.judged.contracts.publisher.expectations.ExpectationsReaderFactory
import com.hltech.judged.contracts.publisher.judged.JudgeD
import org.gradle.api.Plugin
import org.gradle.api.Project

class PublisherPlugin : Plugin<Project> {

    private val capabilitiesReaderFactory = CapabilitiesReaderFactory()
    private val expectationsReaderFactory = ExpectationsReaderFactory()
    private val judgeD = JudgeD()

    override fun apply(project: Project) {
        project.task("publishContracts") { task ->
            task.description = "Publish contracts to remote judge-dredd instance."

            task.doLast {
                println("serviceName=${project.name}")
                println("serviceVersion=${project.version}")
                println("judgeDLocation=${project.properties["judgeDLocation"]}")
                println("capabilities=${project.properties["capabilities"]}")
                println("expectations=${project.properties["expectations"]}")

                val capabilities = getCapabilitiesFrom(project)
                val expectations = getExpectationsFrom(project)

                if (capabilities.isEmpty() && expectations.isEmpty()) {
                    println("No contract to publish")
                } else {
                    judgeD.publishContracts(project, capabilities, expectations)
                }
            }
        }
    }

    private fun getCapabilitiesFrom(project: Project): List<Capabilities> =
        splitProperty(project, CAPABILITIES_KEY)
            .map { capabilitiesReaderFactory.create(it).read(project) }
            .toList()

    private fun getExpectationsFrom(project: Project): List<Expectations> =
        splitProperty(project, EXPECTATIONS_KEY)
            .map { expectationsReaderFactory.create(it).read(project) }
            .flatten()

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