package com.catvasiliy.mydic.domain.model.translation

sealed class Translation(
    open val id: Long = 0,
    open val sourceText: String,
    open val sourceLanguage: Language,
    open val targetLanguage: Language,
    open val translatedAtMillis: Long
)