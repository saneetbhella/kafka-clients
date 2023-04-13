package com.saneetbhella

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import java.time.Duration
import java.util.Collections
import java.util.Properties

fun main() {
    val props = Properties().apply {
        put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
        put(ConsumerConfig.GROUP_ID_CONFIG, "Sunny")
        put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
        put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer::class.java.name)
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

    val kafkaConsumer = KafkaConsumer<String, String>(props)
    kafkaConsumer.subscribe(Collections.singletonList("payments"))

    while (true) {
        val records = kafkaConsumer.poll(Duration.ofMillis(100))
        records.forEach {
            println(it.value())
        }
    }
}
