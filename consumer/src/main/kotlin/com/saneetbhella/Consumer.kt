package com.saneetbhella

import com.saneetbhella.schemas.Payment
import org.apache.kafka.clients.consumer.KafkaConsumer
import java.time.Duration
import java.util.Collections

fun main() {
    val kafkaConsumer = KafkaConsumer<String, Payment>(AppConfig.kafkaProperties)
    kafkaConsumer.subscribe(Collections.singletonList(AppConfig.kafkaProperties.getProperty("topic.name")))

    while (true) {
        val records = kafkaConsumer.poll(Duration.ofMillis(100))
        records.forEach {
            println(it.value().id)
            println(it.value().description)
        }
    }
}
