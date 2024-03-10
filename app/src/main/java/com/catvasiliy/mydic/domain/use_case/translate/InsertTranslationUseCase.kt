package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import javax.inject.Inject

class InsertTranslationUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    suspend operator fun invoke(translation: Translation) {
        when (translation) {
            is ExtendedTranslation -> translateRepository.insertExtendedTranslation(translation)
            is MissingTranslation -> translateRepository.insertMissingTranslation(translation)
        }
    }
}