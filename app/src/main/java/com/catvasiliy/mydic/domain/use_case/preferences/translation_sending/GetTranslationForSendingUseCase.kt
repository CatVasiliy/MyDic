package com.catvasiliy.mydic.domain.use_case.preferences.translation_sending

import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationForSending
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import javax.inject.Inject

class GetTranslationForSendingUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {
    suspend operator fun invoke(): Resource<TranslationForSending> {
        return translateRepository.getTranslationForSending()
    }
}