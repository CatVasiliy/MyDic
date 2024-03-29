package com.catvasiliy.mydic.domain.use_case.preferences.translation_sending

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingInterval
import javax.inject.Inject

class UpdateTranslationSendingIntervalUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(interval: TranslationSendingInterval) {
        preferencesRepository.updateTranslationSendingInterval(interval)
    }
}