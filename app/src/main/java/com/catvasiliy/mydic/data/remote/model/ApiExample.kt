package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiExample(
    @SerialName("example") val entries: List<ApiExampleEntry> = emptyList()
)

@Serializable
data class ApiExampleEntry(
    @SerialName("text") val exampleText: String = ""
)