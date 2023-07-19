package com.catvasiliy.mydic.domain.use_case

import com.catvasiliy.mydic.domain.model.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository

class DeleteTranslation(private val repository: TranslateRepository) {

    suspend operator fun invoke(id: Long, isMissingTranslation: Boolean): Translation {

        val deletedTranslation: Translation

        if (isMissingTranslation) {
            deletedTranslation = repository.getExtendedTranslationById(id)
            repository.deleteMissingTranslationById(id)
        } else {
            deletedTranslation = repository.getMissingTranslationById(id)
            repository.deleteTranslationById(id)
        }

        return deletedTranslation
    }
}