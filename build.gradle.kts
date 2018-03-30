import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.2.21"
	id("org.jetbrains.kotlin.jvm") version kotlinVersion
	id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
	id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("org.springframework.boot") version "2.0.0.RELEASE"
    id("io.spring.dependency-management") version "1.0.4.RELEASE"
}

version = "1.0.0-SNAPSHOT"

tasks.withType<KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
		freeCompilerArgs = listOf("-Xjsr305=strict")
	}
}

val test by tasks.getting(Test::class) {
	useJUnitPlatform()
}

springBoot {
	mainClassName = "examples.basic.BasicApplicationKt"
}

repositories {
	mavenCentral()
	maven { url = uri("https://dl.bintray.com/kotlin/exposed/") }
	maven { url = uri("https://dl.bintray.com/konrad-kaminski/maven") }
}

dependencies {
	compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	compile("org.jetbrains.kotlin:kotlin-reflect")
	compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")

	compile("org.springframework.boot:spring-boot-starter-web")
	compile("org.springframework.boot:spring-boot-starter-webflux")
	compile("org.springframework.boot:spring-boot-starter-data-jpa")
	compile("org.springframework.boot:spring-boot-starter-jdbc")
	compile("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	compile("org.springframework.kotlin:spring-kotlin-coroutine:0.3.4")
	compile("org.springframework.kotlin:spring-webmvc-kotlin-coroutine:0.3.4")

	compile("net.sf.ehcache:ehcache")
	compile("javax.servlet:javax.servlet-api")

	compile("com.h2database:h2")
	compile("de.flapdoodle.embed:de.flapdoodle.embed.mongo")

	compile("org.jetbrains.exposed:exposed:0.8.7")
	compile("org.jetbrains.exposed:spring-transaction:0.8.7")
	compile("joda-time:joda-time")
	compile("com.fasterxml.jackson.module:jackson-module-kotlin")

	testCompile("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "junit")
	}
    testCompile("com.nhaarman:mockito-kotlin:0.9.0")
    testCompile("io.mockk:mockk:1.7.15")
    testCompile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.22.5")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

