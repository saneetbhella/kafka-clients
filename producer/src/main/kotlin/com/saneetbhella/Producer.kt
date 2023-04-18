package com.saneetbhella

import com.saneetbhella.schemas.Payment
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.UUID

fun main() {
    val kafkaProducer = KafkaProducer<String, Payment>(AppConfig.kafkaProperties)

    for (i in 1..1000) {
        val payment = Payment(UUID.randomUUID().toString(), "A payment")
        kafkaProducer.send(ProducerRecord("payments", payment))
        kafkaProducer.flush()
    }
}
