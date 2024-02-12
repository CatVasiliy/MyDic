package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository

class DeleteTranslation(private val repository: TranslateRepository) {

    suspend operator fun invoke(id: Long, isMissingTranslation: Boolean): Translation {

        val deletedTranslation: Translation

        if (isMissingTranslation) {
            deletedTranslation = repository.getMissingTranslationById(id)
            repository.deleteMissingTranslationById(id)
        } else {
            deletedTranslation = repository.getExtendedTranslationById(id)
            repository.deleteTranslationById(id)
        }

        return deletedTranslation
    }
}