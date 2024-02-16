package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import javax.inject.Inject

class DeleteTranslationUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    suspend operator fun invoke(id: Long, isMissingTranslation: Boolean): Translation {

        val deletedTranslation: Translation

        if (isMissingTranslation) {
            deletedTranslation = translateRepository.getMissingTranslationById(id)
            translateRepository.deleteMissingTranslationById(id)
        } else {
            deletedTranslation = translateRepository.getExtendedTranslationById(id)
            translateRepository.deleteTranslationById(id)
        }

        return deletedTranslation
    }
}