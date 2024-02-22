package com.catvasiliy.mydic.domain.model.preferences

import kotlinx.serialization.Serializable

@Serializable
data class TranslationSendingPreferences(
    val isSendingEnabled: Boolean = false,
    val sendingInterval: TranslationSendingInterval = TranslationSendingInterval.MINUTES_1
)
