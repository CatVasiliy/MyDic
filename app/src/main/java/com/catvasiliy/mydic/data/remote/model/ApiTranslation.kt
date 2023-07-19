package com.catvasiliy.mydic.data.remote.model

import com.squareup.moshi.Json

data class ApiTranslation(
    @field:Json(name = "sentences") val primaryTranslation: List<ApiPrimaryTranslation>?,
    @field:Json(name = "dict") val alternativeTranslations: List<ApiAlternativeTranslation>?,
    @field:Json(name = "definitions") val definitions: List<ApiDefinition>?,
    @field:Json(name = "examples") val examples: ApiExample?
)