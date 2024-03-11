package com.catvasiliy.mydic.domain.use_case.preferences.translation_language

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLanguagePreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<LanguagePreferences> {
        return preferencesRepository.getLanguagePreferences()
    }
}