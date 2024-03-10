package com.catvasiliy.mydic.presentation.model.translation

import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.domain.model.translation.language.LanguageCodeNotFoundException

enum class UiLanguage(
    val code: String,
    @StringRes val stringResourceId: Int
) {
    ENGLISH("en", R.string.language_english),
    RUSSIAN("ru", R.string.language_russian),
    GERMAN("de", R.string.language_german),
    FRENCH("fr", R.string.language_french),
    ITALIAN("it", R.string.language_italian),
    SPANISH("es", R.string.language_spanish),
    HINDI("hi", R.string.language_hindi),
    CHINESE("zh", R.string.language_chinese),
    KOREAN("ko", R.string.language_korean),
    JAPANESE("ja", R.string.language_japanese);

    companion object {

        fun fromCode(languageCode: String): UiLanguage? {
            return entries.find { it.code == languageCode }
        }

        fun fromCodeNotNull(languageCode: String): UiLanguage {
            return fromCode(languageCode) ?: throw LanguageCodeNotFoundException(languageCode)
        }
    }
}