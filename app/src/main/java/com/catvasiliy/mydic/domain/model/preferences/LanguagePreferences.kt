package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.translation.Language
import kotlinx.serialization.Serializable

@Serializable
data class LanguagePreferences(
    val defaultSourceLanguage: Language = Language.ENGLISH,
    val defaultTargetLanguage: Language = Language.RUSSIAN
)
