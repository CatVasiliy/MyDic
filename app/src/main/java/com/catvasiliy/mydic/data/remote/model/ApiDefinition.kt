package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiDefinition(
    @SerialName("pos") val partOfSpeech: String = "",
    @SerialName("entry") val entries: List<ApiDefinitionEntry> = emptyList()
)

@Serializable
data class ApiDefinitionEntry(
    @SerialName("gloss") val definitionText: String = "",
    @SerialName("example") val exampleText: String = ""
)
