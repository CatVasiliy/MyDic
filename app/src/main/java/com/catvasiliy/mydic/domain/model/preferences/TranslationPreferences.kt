package com.catvasiliy.mydic.domain.model.preferences

import kotlinx.serialization.Serializable

@Serializable
data class TranslationPreferences(
    val languagePreferences: LanguagePreferences = LanguagePreferences(),
    val translationSendingPreferences: TranslationSendingPreferences = TranslationSendingPreferences()
)
