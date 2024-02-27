package com.catvasiliy.mydic.data.remote.model.deserializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Encoder

abstract class ApiDeserializer<T> : KSerializer<T> {

    protected abstract val serialName: String

    override fun serialize(encoder: Encoder, value: T) {
        throw SerializationException("$serialName is not supposed to be serialized.")
    }
}