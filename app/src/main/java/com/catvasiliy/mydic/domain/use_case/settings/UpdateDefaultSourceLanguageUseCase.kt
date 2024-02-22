package com.catvasiliy.mydic.domain.use_case.settings

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.translation.Language
import javax.inject.Inject

class UpdateDefaultSourceLanguageUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(sourceLanguage: Language) {
        preferencesRepository.updateDefaultSourceLanguage(sourceLanguage)
    }
}