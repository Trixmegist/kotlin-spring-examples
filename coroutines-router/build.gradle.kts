import org.jetbrains.kotlin.gradle.dsl.Coroutines

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.jetbrains.kotlin.plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

kotlin {
    experimental.coroutines = Coroutines.ENABLE
}

repositories {
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/konrad-kaminski/maven") }
}
dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.springframework.boot:spring-boot-starter-webflux")
    compile("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    compile("de.flapdoodle.embed:de.flapdoodle.embed.mongo")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")

    //coroutines
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")
    compile("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:0.22.5")
    compile("org.springframework.kotlin:spring-kotlin-coroutine:0.3.4")
    compile("org.springframework.kotlin:spring-webflux-kotlin-coroutine:0.3.4")
    compile("org.springframework.kotlin:spring-data-mongodb-kotlin-coroutine:0.3.4")
    compile("org.springframework.kotlin:spring-boot-autoconfigure-kotlin-coroutine:0.3.4")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("com.nhaarman:mockito-kotlin:0.9.0")
    testCompile("io.mockk:mockk:1.7.15")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}