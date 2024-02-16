package com.catvasiliy.mydic.domain.use_case.settings

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.settings.TranslationSendingPreferences
import javax.inject.Inject

class ToggleTranslationSendingUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    suspend operator fun invoke(sendingPreferences: TranslationSendingPreferences) {
        preferencesRepository.setIsTranslationSendingEnabled(sendingPreferences.isSendingEnabled)
        preferencesRepository.setTranslationSendingInterval(sendingPreferences.sendingInterval)
    }
}