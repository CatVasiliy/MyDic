package com.catvasiliy.mydic.domain.repository

import com.catvasiliy.mydic.domain.model.preferences.TranslationForSending
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface TranslateRepository {
    fun getTranslationFromApi(
        sourceText: String,
        sourceLanguage: SourceLanguage,
        targetLanguage: TargetLanguage
    ): Flow<Resource<Translation>>

    fun getTranslationsList(): Flow<List<Translation>>
    suspend fun getExtendedTranslationById(id: Long): Translation
    suspend fun deleteTranslationById(id: Long)

    fun updateMissingTranslationFromApi(
        missingTranslation: Translation
    ): Flow<Resource<Translation>>

    suspend fun getMissingTranslationById(id: Long): Translation
    suspend fun deleteMissingTranslationById(id: Long)

    suspend fun insertExtendedTranslation(extendedTranslation: Translation)
    suspend fun insertMissingTranslation(missingTranslation: Translation)

    suspend fun getTranslationForSending(): Resource<TranslationForSending>
}