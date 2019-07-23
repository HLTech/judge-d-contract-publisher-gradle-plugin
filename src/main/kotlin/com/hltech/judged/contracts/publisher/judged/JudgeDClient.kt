package com.hltech.judged.contracts.publisher.judged

import feign.Param
import feign.RequestLine

interface JudgeDClient {

    @RequestLine("POST /contracts/{serviceName}/{serviceVersion}")
    fun publish(
        @Param("serviceName") serviceName: String,
        @Param("serviceVersion") serviceVersion: String,
        contracts: ServiceContractsForm
    )
}