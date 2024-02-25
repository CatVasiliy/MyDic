package com.catvasiliy.mydic.domain.use_case.settings

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import javax.inject.Inject

class UpdateDefaultSourceLanguageUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(sourceLanguage: SourceLanguage) {
        preferencesRepository.updateDefaultSourceLanguage(sourceLanguage)
    }
}