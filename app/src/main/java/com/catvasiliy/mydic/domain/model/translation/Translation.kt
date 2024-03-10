package com.catvasiliy.mydic.domain.model.translation

import com.catvasiliy.mydic.domain.model.translation.language.Language

sealed class Translation {
    abstract val id: Long
    abstract val sourceText: String
    abstract val targetLanguage: Language
    abstract val translatedAtMillis: Long
}