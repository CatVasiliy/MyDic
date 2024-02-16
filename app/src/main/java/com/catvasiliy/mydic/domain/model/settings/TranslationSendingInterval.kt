package com.catvasiliy.mydic.domain.model.settings

import java.util.concurrent.TimeUnit

enum class TranslationSendingInterval(val duration: Long, val timeUnit: TimeUnit) {
    MINUTES_15(15, TimeUnit.MINUTES),
    HOURS_3(3, TimeUnit.HOURS),
    HOURS_6(6, TimeUnit.HOURS),
    HOURS_12(12, TimeUnit.HOURS)
}