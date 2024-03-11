package com.catvasiliy.mydic.domain.use_case.preferences.translation_sending

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTranslationSendingPreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<TranslationSendingPreferences> {
        return preferencesRepository.getTranslationSendingPreferences()
    }
}