package com.catvasiliy.mydic.data.remote.model

import com.squareup.moshi.Json

data class ApiPrimaryTranslation(
    @field:Json(name = "trans") val translationText: String?,
    @field:Json(name = "orig") val sourceText: String?,
    @field:Json(name = "src_translit") val sourceTransliteration: String?
)
