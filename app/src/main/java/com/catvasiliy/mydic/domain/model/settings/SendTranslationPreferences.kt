package com.catvasiliy.mydic.domain.model.settings

import kotlinx.serialization.Serializable

@Serializable
data class SendTranslationPreferences(
    val isSendingEnabled: Boolean = false,
    val period: Period = Period.SECONDS_10
)
