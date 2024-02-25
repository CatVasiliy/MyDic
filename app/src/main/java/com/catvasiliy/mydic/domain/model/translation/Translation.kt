package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TranslationSourceLanguage

sealed class Translation(
    open val id: Long = 0,
    open val sourceText: String,
    open val sourceLanguage: TranslationSourceLanguage,
    open val targetLanguage: TargetLanguage,
    open val translatedAtMillis: Long
)