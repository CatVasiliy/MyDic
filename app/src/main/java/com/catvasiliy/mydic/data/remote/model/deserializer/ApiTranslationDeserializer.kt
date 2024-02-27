package com.catvasiliy.mydic.data.remote.model.deserializer

import com.catvasiliy.mydic.data.remote.model.ApiAlternativeTranslation
import com.catvasiliy.mydic.data.remote.model.ApiDefinition
import com.catvasiliy.mydic.data.remote.model.ApiExample
import com.catvasiliy.mydic.data.remote.model.ApiTranslation
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.decodeStructure

object ApiTranslationDeserializer : ApiDeserializer<ApiTranslation>() {

    override val serialName: String = "ApiTranslation"

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor(serialName) {
        element<List<ApiBaseTranslation>>("sentences")
        element<String>("src")
        element("dict", ApiAlternativeTranslationListDeserializer.descriptor)
        element("definitions", ApiDefinitionListDeserializer.descriptor)
        element<List<ApiExample>>("examples")
    }

    override fun deserialize(decoder: Decoder): ApiTranslation {

        return decoder.decodeStructure(descriptor) {

            var baseTranslationList: List<ApiBaseTranslation>? = null
            var sourceLanguageCode: String? = null
            var alternativeTranslations: List<ApiAlternativeTranslation>? = null
            var definitions: List<ApiDefinition>? = null
            var examples: List<ApiExample>? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {

                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> baseTranslationList = decodeSerializableElement(
                        descriptor,
                        0,
                        ListSerializer(ApiBaseTranslation.serializer())
                    )
                    1 -> sourceLanguageCode = decodeStringElement(
                        descriptor,
                        1
                    )
                    2 -> alternativeTranslations = decodeSerializableElement(
                        descriptor,
                        2,
                        ApiAlternativeTranslationListDeserializer
                    )
                    3 -> definitions = decodeSerializableElement(
                        descriptor, 3,
                        ApiDefinitionListDeserializer
                    )
                    4 -> examples = decodeSerializableElement(
                        descriptor,
                        4,
                        ApiExampleListDeserializer
                    )

                    else -> throw SerializationException("Unexpected element index: $index")
                }
            }

            val translationTextNullable = baseTranslationList?.get(0)?.translationText
            val sourceTextNullable = baseTranslationList?.get(0)?.sourceText
            val sourceTransliteration = baseTranslationList?.get(1)?.sourceTransliteration

            ApiTranslation(
                translationText = requireNotNull(translationTextNullable),
                sourceText = requireNotNull(sourceTextNullable),
                sourceTransliteration = sourceTransliteration,
                sourceLanguageCode = requireNotNull(sourceLanguageCode),
                alternativeTranslations = alternativeTranslations ?: emptyList(),
                definitions = definitions ?: emptyList(),
                examples = examples ?: emptyList()
            )
        }
    }

    @Serializable
    private data class ApiBaseTranslation(
        @SerialName("trans") val translationText: String = "",
        @SerialName("orig") val sourceText: String = "",
        @SerialName("src_translit") val sourceTransliteration: String? = null
    )
}