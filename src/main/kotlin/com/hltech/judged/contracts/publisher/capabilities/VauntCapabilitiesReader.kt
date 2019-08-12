package com.hltech.judged.contracts.publisher.capabilities

import com.hltech.judged.contracts.publisher.vaunt.VauntFileReader
import com.hltech.vaunt.core.VauntSerializer
import org.gradle.api.Project

class VauntCapabilitiesReader : CapabilitiesReader {

    private val fileReader = VauntFileReader()
    private val serializer = VauntSerializer()

    override fun read(project: Project): Capabilities {
        val contracts =  fileReader.read(project)
            .flatMap { it.capabilities.contracts }
            .toList()

        return Capabilities("jms", serializer.serialize(contracts), "application/json")
    }

}