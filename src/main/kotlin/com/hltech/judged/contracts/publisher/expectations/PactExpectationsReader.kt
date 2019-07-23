package com.hltech.judged.contracts.publisher.expectations

import org.gradle.api.Project
import java.io.File

class PactExpectationsReader : ExpectationsReader {

    private val pactFileReader = PactFileReader()

    override fun read(project: Project): List<Expectations> =
        when (val pactsLocation = project.properties[PACTS_LOCATION_KEY]) {
            is String -> readFrom(File(pactsLocation))
            else -> throw IllegalArgumentException("Parameter '$PACTS_LOCATION_KEY' is required for rest expectations")
        }

    private fun readFrom(pactsLocation: File): List<Expectations> {
        if (!pactsLocation.exists()) {
            throw IllegalArgumentException("Directory ${pactsLocation.absolutePath} does not exist!")
        }

        val foundPacts = pactsLocation
            .listFiles { _, name -> name.endsWith(".json") }
            .mapNotNull { pactFileReader.read(it) }
            .toList()

        println("Found ${foundPacts.size} pact files")

        return foundPacts
            .map { Expectations(it.providerName, "rest", it.content, "application/json") }
            .toList()
    }

    private companion object {
        private const val PACTS_LOCATION_KEY = "pactsLocation"
    }
}