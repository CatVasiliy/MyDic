package com.catvasiliy.mydic.domain.model.settings

import java.time.Duration
import java.time.ZonedDateTime

enum class TranslationSendingInterval(
    private val duration: Duration
) {
    MINUTES_1(Duration.ofMinutes(1)),
    MINUTES_2(Duration.ofMinutes(2)),
    HOURS_3(Duration.ofHours(3)),
    HOURS_6(Duration.ofHours(6)),
    HOURS_12(Duration.ofHours(12));

    val millis: Long
        get() = duration.toMillis()

    fun getFromNowMillis(): Long {
        return ZonedDateTime.now().plus(duration).toInstant().toEpochMilli()
    }
}