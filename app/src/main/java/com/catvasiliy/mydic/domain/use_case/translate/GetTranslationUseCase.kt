package com.catvasiliy.mydic.domain.use_case.translate

import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTranslationUseCase @Inject constructor(
    private val translateRepository: TranslateRepository
) {

    operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String
    ): Flow<Resource<Translation>> {
        return translateRepository.getTranslationFromApi(sourceLanguage, targetLanguage, sourceText)
    }

    suspend operator fun invoke(id: Long, isMissingTranslation: Boolean): Translation {
        return if (isMissingTranslation) {
            translateRepository.getMissingTranslationById(id)
        } else {
            translateRepository.getExtendedTranslationById(id)
        }
    }

    operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        translation: Translation
    ): Flow<Resource<Translation>> {

        if (translation !is MissingTranslation) throw IllegalArgumentException()

        return translateRepository.updateMissingTranslationFromApi(
            sourceLanguage,
            targetLanguage,
            translation
        )
    }
}