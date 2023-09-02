package com.saneetbhella

import com.saneetbhella.AppConfig.getConfig
import com.saneetbhella.schemas.Payment
import org.apache.kafka.streams.KeyValue
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorContext
import org.apache.kafka.streams.processor.api.Record
import org.apache.kafka.streams.state.KeyValueStore

class PaymentReadProcessor : Processor<String, Payment, String, Payment> {

    private lateinit var context: ProcessorContext<String, Payment>

    private lateinit var stateStore: KeyValueStore<String, Payment>

    override fun init(context: ProcessorContext<String, Payment>) {
        this.context = context

        stateStore = context.getStateStore(
            AppConfig.kafkaProperties.getConfig("state.store.name")
        ) as KeyValueStore<String, Payment>
    }

    override fun process(record: Record<String, Payment>) {
        stateStore.get(record.key())?.let {
            println("Found ${record.key()} in store, value: ${record.value()}")
        }
        context.forward(record)
    }
}
