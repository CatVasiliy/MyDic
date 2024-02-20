package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import javax.inject.Inject

class GetTranslationUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    suspend operator fun invoke(id: Long, isMissingTranslation: Boolean): Translation {
        return if (isMissingTranslation) {
            translateRepository.getMissingTranslationById(id)
        } else {
            translateRepository.getExtendedTranslationById(id)
        }
    }
}