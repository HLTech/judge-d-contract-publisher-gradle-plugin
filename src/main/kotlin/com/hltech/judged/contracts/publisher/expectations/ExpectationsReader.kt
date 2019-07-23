package com.hltech.judged.contracts.publisher.expectations

import org.gradle.api.Project

interface ExpectationsReader {

    fun read(project: Project): List<Expectations>
}