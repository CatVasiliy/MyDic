package com.catvasiliy.mydic.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiTranslation(
    @SerialName("sentences") val primaryTranslation: List<ApiPrimaryTranslation> = emptyList(),
    @SerialName("dict") val alternativeTranslations: List<ApiAlternativeTranslation> = emptyList(),
    @SerialName("definitions") val definitions: List<ApiDefinition> = emptyList(),
    @SerialName("examples") val examples: ApiExample = ApiExample()
)