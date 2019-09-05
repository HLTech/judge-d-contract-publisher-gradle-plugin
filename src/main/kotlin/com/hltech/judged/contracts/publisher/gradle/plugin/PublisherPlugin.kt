package com.hltech.judged.contracts.publisher.gradle.plugin

import com.hltech.judged.contracts.publisher.Publisher
import com.hltech.judged.contracts.publisher.PublisherProperties
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.lang.IllegalArgumentException

class PublisherPlugin : Plugin<Project> {

    private val publisher = Publisher()

    override fun apply(project: Project) {
        project.task("publishContracts") { task ->
            task.description = "Publish contracts to remote judge-dredd instance."

            task.doLast {
                println("serviceName=${project.name}")
                println("serviceVersion=${project.version}")
                println("judgeDLocation=${project.properties[JUDGED_LOCATION_KEY]}")
                println("capabilities=${project.properties[CAPABILITIES_KEY]}")
                println("expectations=${project.properties[EXPECTATIONS_KEY]}")

                val properties = PublisherProperties(
                    project.name,
                    project.version.toString(),
                    project.properties[JUDGED_LOCATION_KEY]?.toString()
                        ?: throw IllegalArgumentException("Parameter 'judgeDLocation' is required to publish contracts"),
                    splitProperty(project, CAPABILITIES_KEY).toList(),
                    splitProperty(project, EXPECTATIONS_KEY).toList(),
                    project.properties.mapValues { it.value.toString() }
                )

                publisher.publish(properties)
            }
        }
    }

    private fun splitProperty(project: Project, property: String): Set<String> =
        when (val properties = project.properties[property]) {
            is String -> properties.split(",")
                .map { it.trim() }
                .toSet()
            else -> emptySet()
        }

    private companion object {
        private const val JUDGED_LOCATION_KEY = "judgeDLocation"
        private const val CAPABILITIES_KEY = "capabilities"
        private const val EXPECTATIONS_KEY = "expectations"
    }
}