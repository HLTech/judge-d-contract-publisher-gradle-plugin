package com.hltech.judged.contracts.publisher.capabilities

import com.fasterxml.jackson.databind.ObjectMapper
import org.gradle.api.Project
import java.io.File

class SwaggerCapabilitiesReader : CapabilitiesReader {

    override fun read(project: Project): Capabilities =
        when (val swaggerLocation = project.properties[SWAGGER_LOCATION_KEY]) {
            is String -> Capabilities("rest", readFrom(File(swaggerLocation)), "application/json")
            else -> throw IllegalArgumentException("Parameter 'swaggerLocation' is required for rest capabilities")
        }

    private fun readFrom(dir: File): String {
        if (!dir.exists()) {
            throw IllegalArgumentException("Directory ${dir.absolutePath} does not exist!")
        }

        val swaggerFile = File(dir, "swagger.json")
        if (swaggerFile.exists()) {
            println("Found swagger file: ${swaggerFile.absolutePath}")
        } else {
            throw IllegalArgumentException("Cannot find swagger.json file inside dir: ${dir.absolutePath}")
        }

        val mapper = ObjectMapper()
        return mapper.writeValueAsString(mapper.readTree(swaggerFile))
    }

    private companion object {
        private const val SWAGGER_LOCATION_KEY = "swaggerLocation"
    }
}