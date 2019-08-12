package com.hltech.judged.contracts.publisher.gradle.plugin

import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.ClassRule
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*

class PublisherPluginSpec extends Specification {

    @ClassRule
    static WireMockClassRule wireMockRule = new WireMockClassRule(0)

    @Rule
    WireMockClassRule instanceRule = wireMockRule

    @Rule
    TemporaryFolder testProjectDir = new TemporaryFolder()

    void setup() {
        testProjectDir.newFile('build.gradle') << """
            plugins {
                id 'com.hltech.judged.contracts.publisher'
            }
            
            version = '1.0'
        """

        testProjectDir.newFile('settings.gradle') << """
            rootProject.name = 'test-project'
        """
    }

    def "should do nothing when no expectations nor capabilities to publish"() {
        expect:
            GradleRunner.create()
                    .withProjectDir(testProjectDir.root)
                    .withArguments('publishContracts')
                    .withPluginClasspath()
                    .forwardOutput()
                    .build()
                    .task(':publishContracts').getOutcome() == TaskOutcome.SUCCESS
    }

    def "should publish swagger contract when rest capabilities is provided"() {
        given:
            stubFor(
                    post('/contracts/test-project/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities.rest.value',
                                    equalToJson(file('src/test/resources/swagger/swagger.json'))))
                            .withRequestBody(matchingJsonPath('$.capabilities.rest.mimeType',
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath('$.expectations',
                                    equalToJson('{}')))
                            .willReturn(aResponse().withStatus(200))
            )

        expect:
            GradleRunner.create()
                    .withProjectDir(testProjectDir.root)
                    .withArguments('publishContracts',
                            '-Pcapabilities=rest',
                            '-PswaggerLocation=src/test/resources/swagger',
                            "-PjudgeDLocation=http://localhost:${wireMockRule.port()}")
                    .withPluginClasspath()
                    .forwardOutput()
                    .build()
                    .task(':publishContracts').getOutcome() == TaskOutcome.SUCCESS
    }

    def "should publish pacts when rest expectations is provided"() {
        given:
            stubFor(
                    post('/contracts/test-project/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities',
                                    equalToJson('{}')))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Animal Service'].rest.value",
                                    equalToJson(file('src/test/resources/pact/sample-pact.json'))))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Animal Service'].rest.mimeType",
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Events Service'].rest.value",
                                    equalToJson(file('src/test/resources/pact/another-pact.json'))))
                            .withRequestBody(matchingJsonPath("\$.expectations.['Events Service'].rest.mimeType",
                                    equalTo('application/json')))
                            .willReturn(aResponse().withStatus(200))
            )

        expect:
            GradleRunner.create()
                    .withProjectDir(testProjectDir.root)
                    .withArguments('publishContracts',
                            '-Pexpectations=rest',
                            '-PpactsLocation=src/test/resources/pact',
                            "-PjudgeDLocation=http://localhost:${wireMockRule.port()}")
                    .withPluginClasspath()
                    .forwardOutput()
                    .build()
                    .task(':publishContracts').getOutcome() == TaskOutcome.SUCCESS
    }

    def "should publish vaunt capabilities when jms capability is declared"() {
        given:
            stubFor(
                    post('/contracts/test-project/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities.jms.value',
                                    equalToJson("""
                                    [
                                      {
                                        "destinationType": "QUEUE",
                                        "destinationName": "request_for_information_queue",
                                        "message": {
                                          "type": "object",
                                          "id": "urn:jsonschema:com:hltech:vaunt:generator:domain:representation:RepresentationWriterSpec:RequestMessage",
                                          "properties": {
                                            "name": {
                                              "type": "string"
                                            }
                                          }
                                        }
                                      },
                                      {
                                        "destinationType": "TOPIC",
                                        "destinationName": "something_changed_topic",
                                        "message": {
                                          "type": "object",
                                          "id": "urn:jsonschema:com:hltech:vaunt:generator:domain:representation:RepresentationWriterSpec:ChangedEvent",
                                          "properties": {
                                            "timestamp": {
                                              "type": "integer"
                                            }
                                          }
                                        }
                                      }
                                    ]
                                    """)))
                            .withRequestBody(matchingJsonPath('$.capabilities.jms.mimeType',
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath('$.expectations',
                                    equalToJson('{}')))
                            .willReturn(aResponse().withStatus(200))
            )

        expect:
            GradleRunner.create()
                    .withProjectDir(testProjectDir.root)
                    .withArguments('publishContracts',
                            '-Pcapabilities=jms',
                            '-PvauntLocation=src/test/resources/vaunt',
                            "-PjudgeDLocation=http://localhost:${wireMockRule.port()}")
                    .withPluginClasspath()
                    .forwardOutput()
                    .build()
                    .task(':publishContracts').getOutcome() == TaskOutcome.SUCCESS
    }

    def "should publish vaunt expectations when jms expectation is declared"() {
        given:
            stubFor(
                    post('/contracts/test-project/1.0')
                            .withHeader('Content-Type', equalTo('application/json'))
                            .withRequestBody(matchingJsonPath('$.capabilities',
                                    equalToJson('{}')))
                            .withRequestBody(matchingJsonPath('$.expectations.audit-service.jms.value',
                                    equalToJson("""
                                    [
                                      {
                                        "destinationType": "QUEUE",
                                        "destinationName": "audit_queue",
                                        "message": {
                                          "type": "object",
                                          "id": "urn:jsonschema:com:hltech:vaunt:generator:domain:representation:RepresentationWriterSpec:AuditMessage",
                                          "properties": {
                                            "payload": {
                                              "type": "string"
                                            }
                                          }
                                        }
                                      }
                                    ]
                                    """)))
                            .withRequestBody(matchingJsonPath('$.expectations.audit-service.jms.mimeType',
                                    equalTo('application/json')))
                            .withRequestBody(matchingJsonPath('$.expectations.remote-client.jms.value',
                                    equalToJson("""
                                    [
                                      {
                                        "destinationType": "QUEUE",
                                        "destinationName": "reject_information_queue",
                                        "message": {
                                          "type": "object",
                                          "id": "urn:jsonschema:com:hltech:vaunt:generator:domain:representation:RepresentationWriterSpec:RejectMessage",
                                          "properties": {
                                            "reason": {
                                              "type": "string"
                                            },
                                            "code": {
                                              "type": "integer"
                                            }
                                          }
                                        }
                                      },
                                      {
                                        "destinationType": "QUEUE",
                                        "destinationName": "accept_information_queue",
                                        "message": {
                                          "type": "object",
                                          "id": "urn:jsonschema:com:hltech:vaunt:generator:domain:representation:RepresentationWriterSpec:AcceptMessage",
                                          "properties": {
                                            "id": {
                                              "type": "integer"
                                            }
                                          }
                                        }
                                      }
                                    ]
                                    """)))
                            .withRequestBody(matchingJsonPath('$.expectations.remote-client.jms.mimeType',
                                    equalTo('application/json')))
                            .willReturn(aResponse().withStatus(200))
            )

        expect:
            GradleRunner.create()
                    .withProjectDir(testProjectDir.root)
                    .withArguments('publishContracts',
                            '-Pexpectations=jms',
                            '-PvauntLocation=src/test/resources/vaunt',
                            "-PjudgeDLocation=http://localhost:${wireMockRule.port()}")
                    .withPluginClasspath()
                    .forwardOutput()
                    .build()
                    .task(':publishContracts').getOutcome() == TaskOutcome.SUCCESS
    }

    private static String file(String path) {
        return new File(path).text
    }
}
