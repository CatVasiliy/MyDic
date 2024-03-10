package com.catvasiliy.mydic.domain.model.translation.language

enum class Language(val code: String) {
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

        const val AUTO_CODE: String = "auto"

        fun fromCode(languageCode: String): Language? {
            return entries.find { it.code == languageCode }
        }

        fun fromCodeNotNull(languageCode: String): Language {
            return fromCode(languageCode) ?: throw LanguageCodeNotFoundException(languageCode)
        }
    }
}