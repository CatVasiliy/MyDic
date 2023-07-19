package com.catvasiliy.mydic.data.remote.model

import com.squareup.moshi.Json

data class ApiExample(
    @field:Json(name = "example") val entries: List<ApiExampleEntry>?
)

data class ApiExampleEntry(
    @field:Json(name = "text") val exampleText: String?
)