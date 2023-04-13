plugins {
    kotlin("jvm") version "1.8.20"
}

repositories {
    mavenCentral()
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.apache.kafka:kafka-clients:3.4.0")
        implementation("ch.qos.logback:logback-classic:1.4.6")
    }

    tasks.test {
        useJUnitPlatform()
    }
}
