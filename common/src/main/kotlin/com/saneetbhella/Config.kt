package com.saneetbhella

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.util.Properties

object AppConfig {

    private val config = ConfigFactory.load().resolve()

    val kafkaProperties = config.getConfig("kafka").asProperties()

    private fun Config.asProperties(): Properties {
        val props = Properties()
        this.entrySet().forEach {
            props[it.key] = it.value.unwrapped()
        }
        return props
    }

    fun Properties.getConfig(value: String): String = this[value].toString()
}
