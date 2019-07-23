package com.hltech.judged.contracts.publisher.capabilities

import org.gradle.api.Project

interface CapabilitiesReader {

    fun read(project: Project): Capabilities
}