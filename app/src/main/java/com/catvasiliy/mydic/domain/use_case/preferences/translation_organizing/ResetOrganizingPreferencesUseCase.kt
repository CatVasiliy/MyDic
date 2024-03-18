package com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.TranslationOrganizingPreferences
import javax.inject.Inject

class ResetOrganizingPreferencesUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke() {
        preferencesRepository.updateTranslationOrganizingPreferences(
            TranslationOrganizingPreferences.getDefault()
        )
    }
}