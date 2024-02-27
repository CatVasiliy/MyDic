package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiExample(
    @SerialName("text") val exampleText: String
)