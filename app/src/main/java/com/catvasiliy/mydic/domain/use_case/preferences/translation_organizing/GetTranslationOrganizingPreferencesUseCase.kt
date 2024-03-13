package com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.TranslationOrganizingPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTranslationOrganizingPreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<TranslationOrganizingPreferences> {
        return preferencesRepository.getTranslationOrganizingPreferences()
    }
}