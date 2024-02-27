package com.catvasiliy.mydic.data.remote.model.deserializer

import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
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
object ApiAlternativeTranslationListDeserializer
    : ApiDeserializer<List<ApiAlternativeTranslation>>() {

    override val serialName: String = ApiAlternativeTranslationEntry.serializer().descriptor.serialName

    private val apiAlternativeTranslationFullEntryDescriptor = buildClassSerialDescriptor(serialName) {
        element<String>("pos")
        element<List<ApiAlternativeTranslationEntry>>("entry")
    }

    override val descriptor: SerialDescriptor = listSerialDescriptor(
        apiAlternativeTranslationFullEntryDescriptor
    )

    override fun deserialize(decoder: Decoder): List<ApiAlternativeTranslation> {
        val input = decoder as? JsonDecoder

        val alternativeArray = input?.decodeJsonElement()?.jsonArray ?: return emptyList()

        return alternativeArray.flatMap { jsonElement ->
            val partOfSpeech = jsonElement.jsonObject["pos"]?.jsonPrimitive?.content ?: ""

            val alternativeEntryElement = jsonElement.jsonObject["entry"]
                ?: throw SerializationException("Expected \"entry\" JsonElement.")

            val alternativeEntryList = input.json.decodeFromJsonElement(
                ListSerializer(ApiAlternativeTranslationEntry.serializer()),
                alternativeEntryElement
            )

            alternativeEntryList.map { apiAlternativeEntry ->
                ApiAlternativeTranslation(
                    translationText = apiAlternativeEntry.translationText,
                    partOfSpeech = partOfSpeech,
                    synonyms = apiAlternativeEntry.synonyms
                )
            }
        }
    }

    @Serializable
    data class ApiAlternativeTranslationEntry(
        @SerialName("word") val translationText: String = "",
        @SerialName("reverse_translation") val synonyms: List<String> = emptyList()
    )
}