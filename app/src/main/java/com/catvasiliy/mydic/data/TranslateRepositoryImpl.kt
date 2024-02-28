package com.catvasiliy.mydic.data

import com.catvasiliy.mydic.data.local.database.TranslationDao
import com.catvasiliy.mydic.data.local.database.model.CachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedTranslation
import com.catvasiliy.mydic.data.local.database.model.CachedTranslationForSending
import com.catvasiliy.mydic.data.local.database.toCachedMissingTranslation
import com.catvasiliy.mydic.data.local.database.toCachedTranslation
import com.catvasiliy.mydic.data.local.database.toExtendedTranslation
import com.catvasiliy.mydic.data.local.database.toMissingTranslation
import com.catvasiliy.mydic.data.local.database.toSimpleTranslation
import com.catvasiliy.mydic.data.local.database.toTranslationForSending
import com.catvasiliy.mydic.data.remote.TranslateApi
import com.catvasiliy.mydic.data.remote.toExtendedTranslation
import com.catvasiliy.mydic.domain.model.preferences.TranslationForSending
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.language.SourceLanguage
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.language.TargetLanguage
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class TranslateRepositoryImpl @Inject constructor(
    private val translateApi: TranslateApi,
    private val translationDao: TranslationDao
) : TranslateRepository {

    override fun getTranslationFromApi(
        sourceText: String,
        sourceLanguage: SourceLanguage,
        targetLanguage: TargetLanguage
    ): Flow<Resource<Translation>> = flow {

        emit(Resource.Loading())

        val existingCachedTranslation = if (sourceLanguage == SourceLanguage.AUTO) {
            translationDao.getUniqueTranslationAutoDetectLanguage(
                sourceText = sourceText,
                targetLanguage = targetLanguage
            )
        } else {
            translationDao.getUniqueTranslation(
                sourceText = sourceText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage
            )
        }

        if (existingCachedTranslation != null) {
            emit(Resource.Success(existingCachedTranslation.toExtendedTranslation()))
            return@flow
        }

        val existingCachedMissingTranslation = translationDao.getUniqueMissingTranslation(
            sourceText = sourceText,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage
        )

        if (existingCachedMissingTranslation != null) {
            emit(Resource.Success(existingCachedMissingTranslation.toMissingTranslation()))
            return@flow
        }

        try {
            val domainTranslation = translateApi.getTranslation(
                sourceText = sourceText,
                sourceLanguage = sourceLanguage.code,
                targetLanguage = targetLanguage.code
            )
            .toExtendedTranslation(
                targetLanguage = targetLanguage,
                isLanguageDetected = sourceLanguage == SourceLanguage.AUTO
            )

            emit(Resource.Loading(domainTranslation))

            val cachedTranslation = domainTranslation.toCachedTranslation()
            translationDao.insertTranslation(cachedTranslation)

            if (cachedTranslation.baseTranslation.isLanguageDetected) {
                val repeatingTranslationId = translationDao.getRepeatingTranslationId(
                    sourceText = cachedTranslation.baseTranslation.sourceText,
                    sourceLanguage = cachedTranslation.baseTranslation.sourceLanguage,
                    targetLanguage = cachedTranslation.baseTranslation.targetLanguage
                )
                repeatingTranslationId?.let { id ->
                    translationDao.deleteTranslation(id)
                }
            }

            emit(Resource.Success())

        } catch (e: Exception) {
            coroutineContext.ensureActive()

            e.printStackTrace()

            val cachedMissingTranslation = MissingTranslation
                .fromSourceText(sourceText, sourceLanguage, targetLanguage)
                .toCachedMissingTranslation()

            val missingTranslationId = translationDao.insertMissingTranslation(cachedMissingTranslation)

            val missingTranslation = translationDao
                .getMissingTranslationById(missingTranslationId)
                .toMissingTranslation()

            emit(Resource.Error(e.localizedMessage, missingTranslation))
        }
    }



    override fun getTranslationsList(): Flow<List<Translation>> {
        val translationsFlow = translationDao.getTranslations().map {
            it.map(CachedTranslation::toSimpleTranslation)
        }
        val missingTranslationsFlow = translationDao.getMissingTranslations().map {
            it.map(CachedMissingTranslation::toMissingTranslation)
        }
        return translationsFlow.combine(missingTranslationsFlow) { translations, missingTranslations ->
            translations.plus(missingTranslations)
        }
    }

    override suspend fun getExtendedTranslationById(id: Long): ExtendedTranslation {
        return translationDao.getTranslationById(id).toExtendedTranslation()
    }

    override suspend fun deleteTranslationById(id: Long) {
        translationDao.deleteTranslation(id)
    }

    override fun updateMissingTranslationFromApi(
        missingTranslation: MissingTranslation
    ): Flow<Resource<Translation>> = flow {

        emit(Resource.Loading())

        try {
            val domainTranslation = translateApi.getTranslation(
                sourceText = missingTranslation.sourceText,
                sourceLanguage = missingTranslation.sourceLanguage.language.code,
                targetLanguage = missingTranslation.targetLanguage.code
            ).toExtendedTranslation(
                targetLanguage = missingTranslation.targetLanguage,
                isLanguageDetected = missingTranslation.sourceLanguage.language == SourceLanguage.AUTO,
                translatedAtMillis = missingTranslation.translatedAtMillis
            )

            emit(Resource.Loading(domainTranslation))

            val cachedTranslation = domainTranslation.toCachedTranslation()
            translationDao.insertTranslation(cachedTranslation)

            if (cachedTranslation.baseTranslation.isLanguageDetected) {
                val repeatingTranslationId = translationDao.getRepeatingTranslationId(
                    sourceText = cachedTranslation.baseTranslation.sourceText,
                    sourceLanguage = cachedTranslation.baseTranslation.sourceLanguage,
                    targetLanguage = cachedTranslation.baseTranslation.targetLanguage
                )
                repeatingTranslationId?.let { id ->
                    translationDao.deleteTranslation(id)
                }
            }

            translationDao.deleteMissingTranslationById(missingTranslation.id)

            emit(Resource.Success())

        } catch (e: Exception) {
            coroutineContext.ensureActive()

            e.printStackTrace()

            emit(Resource.Error(e.localizedMessage, missingTranslation))
        }
    }

    override suspend fun getMissingTranslationById(id: Long): MissingTranslation {
        return translationDao.getMissingTranslationById(id).toMissingTranslation()
    }

    override suspend fun deleteMissingTranslationById(id: Long) {
        translationDao.deleteMissingTranslationById(id)
    }

    override suspend fun insertExtendedTranslation(extendedTranslation: ExtendedTranslation) {
        translationDao.insertTranslation(extendedTranslation.toCachedTranslation())
    }

    override suspend fun insertMissingTranslation(missingTranslation: MissingTranslation) {
        translationDao.insertMissingTranslation(missingTranslation.toCachedMissingTranslation())
    }

    override suspend fun getTranslationForSending(): Resource<TranslationForSending> {
        var cachedTranslationsForSending = translationDao.getTranslationsForSendingList()

        if (cachedTranslationsForSending.isEmpty()) {
            val translationIds = translationDao.getTranslationIds()

            if (translationIds.isEmpty()) return Resource.Error("There are no saved translations")

            translationIds.forEach { id ->
                val cachedNotificationTranslation = CachedTranslationForSending(
                    id = id
                )
                translationDao.insertTranslationForSending(cachedNotificationTranslation)
            }

            cachedTranslationsForSending = translationDao.getTranslationsForSendingList()
        }

        val cachedTranslationForSending = cachedTranslationsForSending.random()
        translationDao.deleteTranslationForSendingById(cachedTranslationForSending.id)

        val cachedTranslation = translationDao.getTranslationById(cachedTranslationForSending.id)

        return Resource.Success(cachedTranslation.toTranslationForSending())
    }
}