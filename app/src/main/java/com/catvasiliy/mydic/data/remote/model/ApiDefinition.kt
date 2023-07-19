package com.catvasiliy.mydic.data.remote.model

import com.squareup.moshi.Json

data class ApiDefinition(
    @field:Json(name = "pos") val partOfSpeech: String?,
    @field:Json(name = "entry") val entries: List<ApiDefinitionEntry>?
)

data class ApiDefinitionEntry(
    @field:Json(name = "gloss") val definitionText: String?,
    @field:Json(name = "example") val exampleText: String?
)
