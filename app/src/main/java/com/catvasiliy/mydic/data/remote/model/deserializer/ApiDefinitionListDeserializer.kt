package com.catvasiliy.mydic.data.remote.model.deserializer

import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class)
object ApiDefinitionListDeserializer : ApiDeserializer<List<ApiDefinition>>() {

    override val serialName: String = ApiDefinitionEntry.serializer().descriptor.serialName

    private val apiDefinitionFullEntryDescriptor = buildClassSerialDescriptor(serialName) {
        element<String>("pos")
        element<List<ApiDefinitionEntry>>("entry")
    }

    override val descriptor: SerialDescriptor = listSerialDescriptor(apiDefinitionFullEntryDescriptor)

    override fun deserialize(decoder: Decoder): List<ApiDefinition> {
        val input = decoder as? JsonDecoder

        val definitionArray = input?.decodeJsonElement()?.jsonArray ?: return emptyList()

        return definitionArray.flatMap { jsonElement ->
            val partOfSpeech = jsonElement.jsonObject["pos"]?.jsonPrimitive?.content ?: ""

            val definitionElement = jsonElement.jsonObject["entry"]
                ?: throw SerializationException("Expected \"entry\" JsonElement.")

            val definitionEntryList = input.json.decodeFromJsonElement(
                ListSerializer(ApiDefinitionEntry.serializer()),
                definitionElement
            )

            definitionEntryList.map { apiDefinitionEntry ->
                ApiDefinition(
                    definitionText = apiDefinitionEntry.definitionText,
                    partOfSpeech = partOfSpeech,
                    exampleText = apiDefinitionEntry.exampleText
                )
            }
        }
    }

    @Serializable
    private data class ApiDefinitionEntry(
        @SerialName("gloss") val definitionText: String = "",
        @SerialName("example") val exampleText: String = ""
    )
}