package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.Language
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    operator fun invoke(
        sourceLanguage: Language,
        targetLanguage: Language,
        sourceText: String
    ): Flow<Resource<Translation>> = translateRepository.getTranslationFromApi(
        sourceText,
        sourceLanguage,
        targetLanguage
    )
}