package com.saneetbhella.topology

import com.saneetbhella.AppConfig
import com.saneetbhella.AppConfig.getConfig
import com.saneetbhella.processor.PaymentReadProcessor
import com.saneetbhella.schemas.Payment
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Produced

class PaymentTopology(
    private val streamsBuilder: StreamsBuilder,
    private val paymentSerde: SpecificAvroSerde<Payment>
) {

    fun build() = streamsBuilder
        .stream(
            AppConfig.kafkaProperties.getConfig("topic.source.name"),
            Consumed.with(Serdes.String(), paymentSerde)
        )
        .map { key, value ->
            println("Received key: $key, value: $value")
            KeyValue.pair(key, value)
        }
        .process(::PaymentReadProcessor)
        .to(AppConfig.kafkaProperties.getConfig("topic.sink.name"), Produced.with(Serdes.String(), paymentSerde))
}
