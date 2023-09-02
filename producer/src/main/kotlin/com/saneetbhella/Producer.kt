package com.saneetbhella

import com.saneetbhella.schemas.Payment
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID

fun main() {
    val kafkaProducer = KafkaProducer<String, Payment>(AppConfig.kafkaProperties)

    for (i in 1..1000) {
        val uuid = UUID.randomUUID().toString()
        val payment = Payment(uuid, "A payment")
        kafkaProducer.send(ProducerRecord(AppConfig.kafkaProperties.getProperty("topic.name"), uuid, payment))
        kafkaProducer.flush()
    }
}
