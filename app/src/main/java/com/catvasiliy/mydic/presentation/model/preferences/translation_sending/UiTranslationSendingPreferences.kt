package com.catvasiliy.mydic.presentation.model.preferences.translation_sending

import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import com.catvasiliy.mydic.presentation.model.toUiTranslationSendingPreferences

data class UiTranslationSendingPreferences(
    val isSendingEnabled: Boolean,
    val sendingInterval: UiTranslationSendingInterval
) {
    companion object {
        fun getDefault(): UiTranslationSendingPreferences {
            return TranslationSendingPreferences.getDefault().toUiTranslationSendingPreferences()
        }
    }
}
