package com.catvasiliy.mydic.domain.use_case

data class TranslationUseCases(
    val getTranslationsList: GetTranslationsList,
    val getTranslation: GetTranslation,
    val insertTranslation: InsertTranslation,
    val deleteTranslation: DeleteTranslation
)
