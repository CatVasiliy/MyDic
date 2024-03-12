package com.catvasiliy.mydic.domain.use_case.preferences.translation_sorting

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.SourceLanguageFilteringInfo
import javax.inject.Inject

class UpdateSourceLanguageFilteringInfoUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(filteringInfo: SourceLanguageFilteringInfo) {
        preferencesRepository.updateSourceLanguageFilteringInfo(filteringInfo)
    }
}