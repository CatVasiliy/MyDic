package com.catvasiliy.mydic.data.remote.model.deserializer

import com.catvasiliy.mydic.data.remote.model.ApiExample
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.decodeStructure


object ApiExampleListDeserializer : ApiDeserializer<List<ApiExample>>() {

    override val serialName: String = "ApiExampleList"

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(serialName) {
        element<List<ApiExample>>("example")
    }

    override fun deserialize(decoder: Decoder): List<ApiExample> {
        return decoder.decodeStructure(descriptor) {

            var exampleList: List<ApiExample>? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {

                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> exampleList = decodeSerializableElement(descriptor, 0, ListSerializer(ApiExample.serializer()))

                    else -> throw SerializationException("Unexpected element index: $index")
                }
            }

            exampleList ?: emptyList()
        }
    }
}