package com.catvasiliy.mydic.domain.model.translation.language

enum class TargetLanguage(val code: String) {
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
        fun fromCode(code: String): TargetLanguage? {
            return entries.find { it.code == code }
        }
    }
}