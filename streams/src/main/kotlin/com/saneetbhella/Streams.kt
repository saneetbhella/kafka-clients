package com.saneetbhella

import com.saneetbhella.AppConfig.getConfig
import com.saneetbhella.processor.StateStoreProcessorSupplier
import com.saneetbhella.schemas.Payment
import com.saneetbhella.topology.PaymentTopology
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.state.Stores

fun main() {
    val streamsBuilder = StreamsBuilder()

    val paymentSerde: SpecificAvroSerde<Payment> = SpecificAvroSerde<Payment>().apply {
        configure(
            mapOf(SCHEMA_REGISTRY_URL_CONFIG to AppConfig.kafkaProperties.getConfig("schema.registry.url")),
            false
        )
    }

    PaymentTopology(
        streamsBuilder = streamsBuilder,
        paymentSerde = paymentSerde
    ).build()

    val storeBuilder = Stores.keyValueStoreBuilder(
        Stores.inMemoryKeyValueStore(AppConfig.kafkaProperties.getConfig("state.store.name")),
        Serdes.String(),
        paymentSerde
    )

    streamsBuilder.addGlobalStore(
        storeBuilder,
        AppConfig.kafkaProperties.getConfig("topic.sink.name"),
        Consumed.with(Serdes.String(), paymentSerde),
        StateStoreProcessorSupplier()
    )

    KafkaStreams(streamsBuilder.build(), AppConfig.kafkaProperties).run {
        Runtime.getRuntime().addShutdownHook(Thread(::close))
        start()
    }
}
