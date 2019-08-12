package com.hltech.judged.contracts.publisher.expectations

import com.google.common.collect.ArrayListMultimap
import com.hltech.judged.contracts.publisher.vaunt.VauntFileReader
import com.hltech.vaunt.core.VauntSerializer
import com.hltech.vaunt.core.domain.model.Contract
import org.gradle.api.Project

class VauntExpectationsReader : ExpectationsReader {

    private val fileReader = VauntFileReader()
    private val serializer = VauntSerializer()

    override fun read(project: Project): List<Expectations> {
        val providerNameToContracts = fileReader.read(project)
            .map { it.expectations.providerNameToContracts }
            .fold(ArrayListMultimap.create<String, Contract>()) { m1, m2 ->
                m1.putAll(m2)
                return@fold m1
            }

        return providerNameToContracts
            .keySet()
            .map { Expectations(it, "jms", serializer.serialize(providerNameToContracts[it]), "application/json") }
    }
}