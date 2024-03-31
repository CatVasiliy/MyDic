package com.catvasiliy.mydic.domain.model.preferences.translation_sending

import com.catvasiliy.mydic.domain.model.translation.language.Language

data class TranslationForSending(
    val id: Long,
    val sourceText: String,
    val translationText: String,
    val sourceLanguage: Language?
)
