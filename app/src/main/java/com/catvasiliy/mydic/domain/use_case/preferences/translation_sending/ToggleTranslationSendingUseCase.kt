package com.catvasiliy.mydic.domain.use_case.preferences.translation_sending

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import javax.inject.Inject

class ToggleTranslationSendingUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(sendingPreferences: TranslationSendingPreferences) {
        preferencesRepository.updateIsTranslationSendingEnabled(sendingPreferences.isSendingEnabled)
        preferencesRepository.updateTranslationSendingInterval(sendingPreferences.sendingInterval)
    }
}