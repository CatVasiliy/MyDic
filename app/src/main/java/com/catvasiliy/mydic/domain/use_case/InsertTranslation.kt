package com.catvasiliy.mydic.domain.use_case

import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository

class InsertTranslation(private val repository: TranslateRepository) {

    suspend operator fun invoke(translation: Translation) {
        if (translation is MissingTranslation) {
            repository.insertMissingTranslation(translation)
        } else {
            repository.insertExtendedTranslation(translation as ExtendedTranslation)
        }
    }
}