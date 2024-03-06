package com.catvasiliy.mydic.presentation.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.LanguageCodeNotFoundException

enum class UiTargetLanguage(val code: String) {
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
        fun fromCode(code: String): UiTargetLanguage {
            return entries.find { it.code == code } ?: throw LanguageCodeNotFoundException(code)
        }
    }
}