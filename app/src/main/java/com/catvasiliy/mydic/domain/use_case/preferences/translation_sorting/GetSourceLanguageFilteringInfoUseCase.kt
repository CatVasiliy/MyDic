package com.catvasiliy.mydic.domain.use_case.preferences.translation_sorting

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.SourceLanguageFilteringInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSourceLanguageFilteringInfoUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    operator fun invoke(): Flow<SourceLanguageFilteringInfo> {
        return preferencesRepository.getSourceLanguageFilteringInfo()
    }
}