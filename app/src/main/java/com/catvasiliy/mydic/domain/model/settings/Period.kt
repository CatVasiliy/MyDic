package com.catvasiliy.mydic.domain.model.settings

enum class Period(val millis: Long) {
    SECONDS_10(1000 * 10),
    HOURS_3(1000 * 60 * 60 * 3),
    HOURS_6(1000 * 60 * 60 * 6),
    HOURS_12(1000 * 60 * 60 * 12)
}