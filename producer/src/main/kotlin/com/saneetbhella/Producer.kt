package com.saneetbhella

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

fun main() {
    val props = Properties().apply {
        put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java.name)
        put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_SSL")
        put(SaslConfigs.SASL_MECHANISM, "PLAIN")
        put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "/Users/SaneetBhella/Projects/kafka-docker/certs/kafka/server.keystore.jks")
        put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "kafkadocker")
        put(SslConfigs.SSL_KEY_PASSWORD_CONFIG, "kafkadocker")
        put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "/Users/SaneetBhella/Projects/kafka-docker/certs/kafka/server.truststore.jks")
        put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "kafkadocker")
        put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "")
        val config = """
            org.apache.kafka.common.security.plain.PlainLoginModule required username='admin' password='admin-secret';
        """.trimIndent()
        put(SaslConfigs.SASL_JAAS_CONFIG, config)
    }

    val kafkaProducer = KafkaProducer<String, String>(props)

    for (i in 1..1000) {
        kafkaProducer.send(ProducerRecord("payments", "key1", "hello world"))
        kafkaProducer.flush()
    }
}
