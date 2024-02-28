package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiBaseTranslation(
    @SerialName("orig") val sourceText: String,
    @SerialName("trans") val translationText: String,
    @SerialName("src_translit") val sourceTransliteration: String?
)
