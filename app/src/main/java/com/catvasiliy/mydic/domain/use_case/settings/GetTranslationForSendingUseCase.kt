package com.catvasiliy.mydic.domain.use_case.settings

import com.catvasiliy.mydic.domain.model.settings.TranslationForSending
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import javax.inject.Inject

class GetTranslationForSendingUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {
    suspend operator fun invoke(): TranslationForSending {
        return translateRepository.getTranslationForSending()
    }
}