package com.catvasiliy.mydic.domain.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class TranslationSendingPreferences(
    val isSendingEnabled: Boolean = false,
    val sendingInterval: TranslationSendingInterval = TranslationSendingInterval.MINUTES_1
)
