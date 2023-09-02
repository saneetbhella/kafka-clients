package com.saneetbhella

import com.saneetbhella.AppConfig.getConfig
import com.saneetbhella.schemas.Payment
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.Record
import org.apache.kafka.streams.state.KeyValueStore

class PaymentWriteProcessor : Processor<String, Payment, Void, Void> {

    private lateinit var stateStore: KeyValueStore<String, Payment>

    override fun init(context: ProcessorContext<Void, Void>) {
        stateStore = context.getStateStore(
            AppConfig.kafkaProperties.getConfig("state.store.name")
        ) as KeyValueStore<String, Payment>
    }

    override fun process(record: Record<String, Payment>) {
        println("Saving ${record.key()}")
        stateStore.put(record.key(), record.value())
    }
}
