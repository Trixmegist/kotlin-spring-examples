import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.2.21"
    kotlin("jvm") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion apply false
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion apply false
    id("org.springframework.boot") version "2.0.0.RELEASE" apply false
    id("io.spring.dependency-management") version "1.0.4.RELEASE" apply false
}

allprojects {
    version = "1.0"

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            freeCompilerArgs = listOf("-Xjsr305=strict")
        }
    }

//    val test by tasks.getting(Test::class) {
//        useJUnitPlatform()
//    }
}

repositories {
    mavenCentral()
}
dependencies {
}

