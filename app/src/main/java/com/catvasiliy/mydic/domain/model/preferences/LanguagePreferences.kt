package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.translation.language.Language
import kotlinx.serialization.Serializable

@Serializable
data class LanguagePreferences(
    val defaultSourceLanguage: Language? = null,
    val defaultTargetLanguage: Language = Language.ENGLISH
)
