package com.catvasiliy.mydic.presentation.model.translation

enum class UiSourceLanguage(val code: String) {
    AUTO("auto"),
    ENGLISH("en"),
    RUSSIAN("ru"),
    GERMAN("de"),
    FRENCH("fr"),
    ITALIAN("it"),
    SPANISH("es"),
    HINDI("hi"),
    CHINESE("zh"),
    KOREAN("ko"),
    JAPANESE("ja");

    companion object {
        fun fromCode(code: String): UiSourceLanguage? {
            return entries.find { it.code == code }
        }
    }
}