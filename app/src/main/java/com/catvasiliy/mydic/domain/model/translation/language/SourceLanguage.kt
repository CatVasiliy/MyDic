package com.catvasiliy.mydic.domain.model.translation.language

enum class SourceLanguage(val code: String) {
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
        fun fromCode(code: String): SourceLanguage? {
            return entries.find { it.code == code }
        }
    }
}

