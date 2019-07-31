package com.hltech.judged.contracts.publisher.capabilities

import com.hltech.judged.contracts.publisher.vaunt.VauntFileReader
import com.hltech.judged.contracts.publisher.vaunt.vauntObjectMapper
import org.gradle.api.Project

class VauntCapabilitiesReader : CapabilitiesReader {

    private val fileReader = VauntFileReader()
    private val mapper = vauntObjectMapper()

    override fun read(project: Project): Capabilities {
        val contracts =  fileReader.read(project)
            .flatMap { it.capabilities.contracts }
            .toList()

        return Capabilities("jms", mapper.writeValueAsString(contracts), "application/json")
    }

}