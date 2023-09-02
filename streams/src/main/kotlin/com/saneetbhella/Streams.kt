package com.saneetbhella

import com.saneetbhella.AppConfig.getConfig
import com.saneetbhella.schemas.Payment
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.state.Stores

fun main() {
    val streamsBuilder = StreamsBuilder()

    val valueAvroSerde: SpecificAvroSerde<Payment> = SpecificAvroSerde<Payment>().apply {
        configure(
            mapOf(SCHEMA_REGISTRY_URL_CONFIG to AppConfig.kafkaProperties.getConfig("schema.registry.url")),
            false
        )
    }

    streamsBuilder
        .stream(
            AppConfig.kafkaProperties.getConfig("topic.source.name"),
            Consumed.with(Serdes.String(), valueAvroSerde)
        )
        .map { key, value ->
            println("Received key: $key, value: $value")
            KeyValue.pair(key, value)
        }
        .process(::PaymentReadProcessor)
        .to(AppConfig.kafkaProperties.getConfig("topic.sink.name"), Produced.with(Serdes.String(), valueAvroSerde))

    val storeBuilder = Stores.keyValueStoreBuilder(
        Stores.inMemoryKeyValueStore(AppConfig.kafkaProperties.getConfig("state.store.name")),
        Serdes.String(),
        valueAvroSerde
    )

    streamsBuilder.addGlobalStore(
        storeBuilder,
        AppConfig.kafkaProperties.getConfig("topic.sink.name"),
        Consumed.with(Serdes.String(), valueAvroSerde),
        StateStoreProcessorSupplier()
    )

    val streams = KafkaStreams(streamsBuilder.build(), AppConfig.kafkaProperties)

    Runtime.getRuntime().addShutdownHook(Thread(streams::close));

    streams.start()
}
