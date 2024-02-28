package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiAlternativeTranslation(
    @SerialName("word") val translationText: String,
    @SerialName("pos") val partOfSpeech: String,
    @SerialName("reverse_translation") val synonyms: List<String>
)
