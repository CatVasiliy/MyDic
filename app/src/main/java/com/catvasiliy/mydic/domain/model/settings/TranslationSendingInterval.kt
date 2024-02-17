package com.catvasiliy.mydic.domain.model.settings

enum class TranslationSendingInterval(val durationMillis: Long) {
    MINUTES_1(1 * 60 * 1000),
    MINUTES_2(2 * 60 * 1000),
    HOURS_3(3 * 60 * 60 * 1000),
    HOURS_6(6 * 60 * 60 * 1000),
    HOURS_12(12 * 60 * 60 * 1000)
}