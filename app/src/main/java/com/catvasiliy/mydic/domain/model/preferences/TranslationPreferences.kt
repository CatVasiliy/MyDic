package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.TranslationOrganizingPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import kotlinx.serialization.Serializable

@Serializable
data class TranslationPreferences(
    val languagePreferences: LanguagePreferences,
    val organizingPreferences: TranslationOrganizingPreferences,
    val translationSendingPreferences: TranslationSendingPreferences
) {
    companion object {
        fun getDefault(): TranslationPreferences =
            TranslationPreferences(
                languagePreferences = LanguagePreferences.getDefault(),
                organizingPreferences = TranslationOrganizingPreferences.getDefault(),
                translationSendingPreferences = TranslationSendingPreferences.getDefault()
            )
    }
}
