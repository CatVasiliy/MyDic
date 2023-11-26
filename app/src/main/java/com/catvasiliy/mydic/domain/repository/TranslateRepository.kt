package com.catvasiliy.mydic.domain.repository

import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TranslateRepository {
    fun getTranslationFromApi(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String
    ): Flow<Resource<Translation>>

    fun getTranslationsList(): Flow<List<Translation>>
    suspend fun getExtendedTranslationById(id: Long): ExtendedTranslation
    suspend fun deleteTranslationById(id: Long)

    fun updateMissingTranslationFromApi(
        sourceLanguage: String,
        targetLanguage: String,
        missingTranslation: MissingTranslation
    ): Flow<Resource<Translation>>

    suspend fun getMissingTranslationById(id: Long): MissingTranslation
    suspend fun deleteMissingTranslationById(id: Long)

    suspend fun insertExtendedTranslation(extendedTranslation: ExtendedTranslation)
    suspend fun insertMissingTranslation(missingTranslation: MissingTranslation)
}