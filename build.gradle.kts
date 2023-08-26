plugins {
    kotlin("jvm") version "1.8.20"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.7.0"
}

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("com.github.davidmc24.gradle.plugin.avro")
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://packages.confluent.io/maven") }
    }

    dependencies {
        // Config
        implementation("com.typesafe:config:1.4.2")

        // Kafka
        implementation("org.apache.kafka:kafka-clients:3.5.1")
        implementation("org.apache.kafka:kafka-streams:3.5.1")

        // Kafka Avro
        implementation("org.apache.avro:avro:1.11.1")
        implementation("io.confluent:kafka-avro-serializer:5.3.0")
        implementation("io.confluent:kafka-streams-avro-serde:7.4.0")

        // Logging
        implementation("ch.qos.logback:logback-classic:1.4.6")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
