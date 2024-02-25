package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import kotlinx.serialization.Serializable

@Serializable
data class LanguagePreferences(
    val defaultSourceLanguage: SourceLanguage = SourceLanguage.AUTO,
    val defaultTargetLanguage: TargetLanguage = TargetLanguage.ENGLISH
)
