package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.TranslationOrganizingPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import kotlinx.serialization.Serializable

@Serializable
data class TranslationPreferences(
    val languagePreferences: LanguagePreferences = LanguagePreferences(),
    val organizingPreferences: TranslationOrganizingPreferences = TranslationOrganizingPreferences(),
    val translationSendingPreferences: TranslationSendingPreferences = TranslationSendingPreferences()
)
