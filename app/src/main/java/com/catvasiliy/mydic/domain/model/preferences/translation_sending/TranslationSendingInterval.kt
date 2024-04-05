package com.catvasiliy.mydic.domain.model.preferences.translation_sending

import java.time.Duration
import java.time.ZonedDateTime

enum class TranslationSendingInterval(
    private val duration: Duration
) {
    HOURS_3(Duration.ofHours(3)),
    HOURS_6(Duration.ofHours(6)),
    HOURS_12(Duration.ofHours(12)),
    DAYS_1(Duration.ofDays(1));

    val millis: Long
        get() = duration.toMillis()

    fun getFromNowMillis(): Long {
        return ZonedDateTime.now().plus(duration).toInstant().toEpochMilli()
    }
}