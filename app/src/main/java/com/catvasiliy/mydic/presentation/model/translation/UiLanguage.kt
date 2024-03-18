package com.catvasiliy.mydic.presentation.model.translation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.domain.model.translation.language.LanguageCodeNotFoundException

enum class UiLanguage(
    val code: String,
    @DrawableRes val drawableResId: Int,
    @StringRes val stringResId: Int
) {
    ENGLISH("en", R.drawable.language_icon_great_britain, R.string.language_english),
    RUSSIAN("ru", R.drawable.language_icon_russia, R.string.language_russian),
    GERMAN("de", R.drawable.language_icon_germany, R.string.language_german),
    FRENCH("fr", R.drawable.language_icon_france, R.string.language_french),
    ITALIAN("it", R.drawable.language_icon_italy, R.string.language_italian),
    SPANISH("es", R.drawable.language_icon_spain, R.string.language_spanish),
    HINDI("hi", R.drawable.language_icon_india, R.string.language_hindi),
    CHINESE("zh", R.drawable.language_icon_china, R.string.language_chinese),
    KOREAN("ko", R.drawable.language_icon_south_korea, R.string.language_korean),
    JAPANESE("ja", R.drawable.language_icon_japan, R.string.language_japanese);

    companion object {

        fun fromCode(languageCode: String): UiLanguage? {
            return entries.find { it.code == languageCode }
        }

        fun fromCodeNotNull(languageCode: String): UiLanguage {
            return fromCode(languageCode) ?: throw LanguageCodeNotFoundException(languageCode)
        }
    }
}