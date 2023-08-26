package com.saneetbhella

import com.saneetbhella.schemas.Payment
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.StreamsConfig
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream

fun main() {
    val streamsBuilder = StreamsBuilder()

    val valueAvroSerde: SpecificAvroSerde<Payment> = SpecificAvroSerde<Payment>().apply {
        configure(mapOf(SCHEMA_REGISTRY_URL_CONFIG to AppConfig.kafkaProperties["schema.registry.url"]), false)
    }

    val stream: KStream<String, Payment> = streamsBuilder
        .stream(
            "payments",
            Consumed.with(Serdes.String(), valueAvroSerde)
        )

    StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG

    stream.foreach { key, value ->
        println("key: $key")
        println("value: $value")
    }

    val streams = KafkaStreams(streamsBuilder.build(), AppConfig.kafkaProperties)

    streams.start()
}
