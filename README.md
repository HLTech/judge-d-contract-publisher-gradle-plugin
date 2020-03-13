# judge-d-contract-publisher-gradle-plugin [![Build Status](https://travis-ci.org/HLTech/judge-d-contract-publisher-gradle-plugin.svg?branch=master)](https://travis-ci.org/HLTech/judge-d-contract-publisher-gradle-plugin) [![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/com/hltech/judged/contracts/publisher/com.hltech.judged.contracts.publisher.gradle.plugin/maven-metadata.xml.svg?label=gradle)](https://plugins.gradle.org/plugin/com.hltech.judged.contracts.publisher)

Gradle plugin to help integrate your project with judge-d in order to validate communication between microservices.

## Usage

Add plugin to your project using plugins DSL:
```groovy
plugins {
  id "com.hltech.judged.contracts.publisher" version "1.0.5"
}
```

or legacy plugin application:
```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.hltech:judge-d-contract-publisher-gradle-plugin:1.0.5"
  }
}

apply plugin: "com.hltech.judged.contracts.publisher"
```

and run `publishContracts` task:
```sh
./gradlew publishContracts 
  -PjudgeDLocation=https://judge-d-ui.herokuapp.com/ 
  -Pcapabilities=rest 
  -PswaggerLocation=./build/swagger/ 
  -Pexpectations=rest 
  -PpactsLocation=./build/pacts/
```

## Parameters
- **judgeDLocation** - url of judge-d instance to which you contracts will be uploaded (required)
- **expectations** - list of comma separated values which determines expectations of you service against providers
- **capabilities** - list of comma separated values which determines what capabalities your service expose

## Expectations
Depending on what values you provide as expectations you also need to pass some additional parameters to make things work:
- **pactsLocation** - required for **rest** expectation, points to directory where pacts files are stored
- **vauntLocation** - required for **jms** expectation, points to directory where vaunt files are stored

## Capabilities
Similar to expectations, you need to pass additional parameters for capabilities also:
- **swaggerLocation** - required for **rest** capability, points to directory where swagger specification is stored
- **vauntLocation** - required for **jms** capability, points to directory where vaunt files are stored

## Useful links
- [judge-d](https://github.com/HLTech/judge-d) - tool used to test contracts between microservices
- [pact-gen](https://github.com/HLTech/pact-gen) - library for generating pact files out of Feign clients
- [vaunt](https://github.com/HLTech/vaunt) - toolkit to define and validate contracts in JMS
