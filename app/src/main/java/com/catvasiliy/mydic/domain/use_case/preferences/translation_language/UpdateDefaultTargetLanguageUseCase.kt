package com.catvasiliy.mydic.domain.use_case.preferences.translation_language

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.translation.language.Language
import javax.inject.Inject

class UpdateDefaultTargetLanguageUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(targetLanguage: Language) {
        preferencesRepository.updateDefaultTargetLanguage(targetLanguage)
    }
}