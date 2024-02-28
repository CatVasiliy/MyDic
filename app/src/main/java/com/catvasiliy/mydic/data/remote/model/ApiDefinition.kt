package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiDefinition(
    @SerialName("gloss") val definitionText: String,
    @SerialName("pos") val partOfSpeech: String,
    @SerialName("example") val exampleText: String?
)