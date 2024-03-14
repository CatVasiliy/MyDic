package com.catvasiliy.mydic.domain.model.preferences

import com.catvasiliy.mydic.domain.model.translation.language.Language
import kotlinx.serialization.Serializable
import java.util.Locale

@Serializable
data class LanguagePreferences(
    val defaultSourceLanguage: Language?,
    val defaultTargetLanguage: Language
) {
    companion object {
        fun getDefault(): LanguagePreferences {
            val deviceLanguageCode = Locale.getDefault().language
            val defaultTargetLanguage = Language.fromCode(deviceLanguageCode) ?: Language.ENGLISH

            return LanguagePreferences(
                defaultSourceLanguage = null,
                defaultTargetLanguage = defaultTargetLanguage
            )
        }
    }
}
