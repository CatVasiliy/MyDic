package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TranslateUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    operator fun invoke(
        sourceLanguage: SourceLanguage,
        targetLanguage: TargetLanguage,
        sourceText: String
    ): Flow<Resource<Translation>> = translateRepository.getTranslationFromApi(
        sourceText,
        sourceLanguage,
        targetLanguage
    )
}