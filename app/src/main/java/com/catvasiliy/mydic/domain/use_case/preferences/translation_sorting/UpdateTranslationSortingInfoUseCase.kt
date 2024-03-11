package com.catvasiliy.mydic.domain.use_case.preferences.translation_sorting

import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.TranslationSortingInfo
import javax.inject.Inject

class UpdateTranslationSortingInfoUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(sortingInfo: TranslationSortingInfo) {
        preferencesRepository.updateTranslationSortingInfo(sortingInfo)
    }
}