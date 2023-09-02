package com.saneetbhella

import com.saneetbhella.schemas.Payment
import org.apache.kafka.streams.processor.api.Processor
import org.apache.kafka.streams.processor.api.ProcessorSupplier

class StateStoreProcessorSupplier : ProcessorSupplier<String, Payment, Void, Void> {

    override fun get(): Processor<String, Payment, Void, Void> = PaymentWriteProcessor()
}
