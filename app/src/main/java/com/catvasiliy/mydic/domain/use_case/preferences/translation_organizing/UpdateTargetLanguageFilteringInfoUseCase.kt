package com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.TargetLanguageFilteringInfo
import javax.inject.Inject

class UpdateTargetLanguageFilteringInfoUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(filteringInfo: TargetLanguageFilteringInfo) {
        preferencesRepository.updateTargetLanguageFilteringInfo(filteringInfo)
    }
}