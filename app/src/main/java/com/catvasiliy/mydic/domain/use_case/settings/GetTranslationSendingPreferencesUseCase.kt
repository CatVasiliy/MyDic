package com.catvasiliy.mydic.domain.use_case.settings

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTranslationSendingPreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {
    operator fun invoke(): Flow<TranslationSendingPreferences> {
        return preferencesRepository.getTranslationSendingPreferences()
    }
}