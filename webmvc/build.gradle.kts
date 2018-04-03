import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.jetbrains.kotlin.plugin.jpa")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

springBoot {
    mainClassName = "examples.webmvc.example1.Application"
}

repositories {
    mavenCentral()
    maven { url = uri("https://dl.bintray.com/kotlin/exposed/") }
}
dependencies {
    compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-data-jpa")
    compile("javax.servlet:javax.servlet-api")
    compile("com.h2database:h2")
    compile("org.jetbrains.exposed:exposed:0.8.7")
    compile("org.jetbrains.exposed:spring-transaction:0.8.7")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin")

    testCompile("org.springframework.boot:spring-boot-starter-test")
    testCompile("com.nhaarman:mockito-kotlin:0.9.0")
    testCompile("io.mockk:mockk:1.7.15")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}