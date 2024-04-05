package com.catvasiliy.mydic.domain.model.preferences.translation_sending

import kotlinx.serialization.Serializable

@Serializable
data class TranslationSendingPreferences(
    val isSendingEnabled: Boolean,
    val sendingInterval: TranslationSendingInterval
) {
    companion object {
        fun getDefault() = TranslationSendingPreferences(
            isSendingEnabled = false,
            sendingInterval = TranslationSendingInterval.HOURS_6
        )
    }
}
