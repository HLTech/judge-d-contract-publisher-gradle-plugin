package com.hltech.judged.contracts.publisher.judged

import com.hltech.judged.contracts.publisher.capabilities.Capabilities
import feign.Feign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.gradle.api.Project

class JudgeD {

    fun publishContracts(project: Project, capabilities: List<Capabilities>) {
        val capabilitiesMap = capabilities
            .map { it.communicationInterface to ContractForm(it.value, it.mimeType) }
            .toMap()

        val contractsForm = ServiceContractsForm(capabilitiesMap, emptyMap())

        val judgeDLocation = project.properties["judgeDLocation"]?.toString()
            ?: throw IllegalArgumentException("Parameter 'judgeDLocation' is required to publish contracts")

        Feign.builder()
            .encoder(JacksonEncoder())
            .decoder(JacksonDecoder())
            .target(JudgeDClient::class.java, judgeDLocation)
            .publish(project.name, project.version.toString(), contractsForm)
    }
}