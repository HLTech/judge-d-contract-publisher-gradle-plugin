package com.hltech.judged.contracts.publisher.vaunt

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

fun vauntObjectMapper(): ObjectMapper = ObjectMapper().registerModule(GuavaModule()).registerModule(JavaTimeModule())