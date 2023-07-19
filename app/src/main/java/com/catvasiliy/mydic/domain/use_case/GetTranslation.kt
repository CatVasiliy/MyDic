package com.catvasiliy.mydic.domain.use_case

import com.catvasiliy.mydic.domain.model.MissingTranslation
import com.catvasiliy.mydic.domain.model.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetTranslation(private val repository: TranslateRepository) {

    operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String
    ): Flow<Resource<Translation>> {
        return repository.getTranslationFromApi(sourceLanguage, targetLanguage, sourceText)
    }

    suspend operator fun invoke(id: Long, isMissingTranslation: Boolean): Translation {
        return if (isMissingTranslation) {
            repository.getMissingTranslationById(id)
        } else {
            repository.getExtendedTranslationById(id)
        }
    }

    operator fun invoke(
        sourceLanguage: String,
        targetLanguage: String,
        translation: Translation
    ): Flow<Resource<Translation>> {

        if (translation !is MissingTranslation) throw IllegalArgumentException()

        return repository.updateMissingTranslationFromApi(
            sourceLanguage,
            targetLanguage,
            translation
        )
    }
}