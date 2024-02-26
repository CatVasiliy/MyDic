package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

@Serializable(with = ApiTranslationSerializer::class)
data class ApiTranslation(
    val sourceText: String,
    val translationText: String,
    val sourceTransliteration: String?,
    val sourceLanguageCode: String,
    val alternativeTranslations: List<ApiAlternativeTranslation>,
    val definitions: List<ApiDefinition>,
    val examples: ApiExample
)

object ApiTranslationSerializer : KSerializer<ApiTranslation> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ApiTranslation") {
        element<List<ApiPrimaryTranslation>>("sentences")
        element<String>("src")
        element<List<ApiAlternativeTranslation>>("dict")
        element<List<ApiDefinition>>("definitions")
        element<ApiExample>("examples")
    }

    override fun serialize(encoder: Encoder, value: ApiTranslation) {
        throw SerializationException("ApiTranslation is not supposed to be serialized.")
    }

    override fun deserialize(decoder: Decoder): ApiTranslation {

        return decoder.decodeStructure(descriptor) {

            var primaryTranslationList: List<ApiPrimaryTranslation>? = null
            var sourceLanguageCode: String? = null
            var alternativeTranslations: List<ApiAlternativeTranslation>? = null
            var definitions: List<ApiDefinition>? = null
            var examples: ApiExample? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {

                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> primaryTranslationList = decodeSerializableElement(descriptor, 0, ListSerializer(ApiPrimaryTranslation.serializer()))
                    1 -> sourceLanguageCode = decodeStringElement(descriptor, 1)
                    2 -> alternativeTranslations = decodeSerializableElement(descriptor, 0, ListSerializer(ApiAlternativeTranslation.serializer()))
                    3 -> definitions = decodeSerializableElement(descriptor, 0, ListSerializer(ApiDefinition.serializer()))
                    4 -> examples = decodeSerializableElement(descriptor, 0, ApiExample.serializer())

                    else -> throw SerializationException("Unexpected element index: $index")
                }
            }

            val translationTextNullable = primaryTranslationList?.get(0)?.translationText
            val sourceTextNullable = primaryTranslationList?.get(0)?.sourceText
            val sourceTransliteration = primaryTranslationList?.get(1)?.sourceTransliteration

            ApiTranslation(
                translationText = requireNotNull(translationTextNullable),
                sourceText = requireNotNull(sourceTextNullable),
                sourceTransliteration = sourceTransliteration,
                sourceLanguageCode = requireNotNull(sourceLanguageCode),
                alternativeTranslations = alternativeTranslations ?: emptyList(),
                definitions = definitions ?: emptyList(),
                examples = examples ?: ApiExample()
            )
        }
    }
}