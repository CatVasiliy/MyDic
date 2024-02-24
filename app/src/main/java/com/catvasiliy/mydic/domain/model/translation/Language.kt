package com.catvasiliy.mydic.domain.model.translation

enum class Language(val code: String) {
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
        fun fromCode(code: String): Language? {
            return entries.find { it.code == code }
        }
    }
}