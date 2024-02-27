package com.catvasiliy.mydic.data.remote.model

import com.catvasiliy.mydic.data.remote.model.deserializer.ApiTranslationDeserializer
import kotlinx.serialization.Serializable

@Serializable(with = ApiTranslationDeserializer::class)
data class ApiTranslation(
    val sourceText: String,
    val translationText: String,
    val sourceTransliteration: String?,
    val sourceLanguageCode: String,
    val alternativeTranslations: List<ApiAlternativeTranslation>,
    val definitions: List<ApiDefinition>,
    val examples: List<ApiExample>
)