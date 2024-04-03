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
import com.catvasiliy.mydic.data.remote.toTranslation
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationForSending
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.model.translation.language.Language
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class TranslateRepositoryImpl @Inject constructor(
    private val translateApi: TranslateApi,
    private val translationDao: TranslationDao
) : TranslateRepository {

    override fun getTranslationFromApi(
        sourceText: String,
        sourceLanguage: Language?,
        targetLanguage: Language
    ): Flow<Resource<Translation>> = flow {

        val existingCachedTranslation = if (sourceLanguage == null) {
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

        val translatedAtMillis = Date().time

        try {
            val domainTranslation = translateApi.getTranslation(
                sourceText = sourceText,
                sourceLanguage = sourceLanguage?.code ?: Language.AUTO_CODE,
                targetLanguage = targetLanguage.code
            )
            .toTranslation(
                targetLanguage = targetLanguage,
                isLanguageDetected = sourceLanguage == null,
                translatedAtMillis = translatedAtMillis
            )

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

            emit(Resource.Success(domainTranslation))

        } catch (e: Exception) {
            coroutineContext.ensureActive()

            e.printStackTrace()

            val cachedMissingTranslation = MissingTranslation
                .createNewMissingTranslation(
                    sourceText = sourceText,
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    translatedAtMillis = translatedAtMillis
                )
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

        val sourceLanguageCode = missingTranslation.sourceLanguage?.code ?: Language.AUTO_CODE

        try {
            val domainTranslation = translateApi.getTranslation(
                sourceText = missingTranslation.sourceText,
                sourceLanguage = sourceLanguageCode,
                targetLanguage = missingTranslation.targetLanguage.code
            ).toTranslation(
                targetLanguage = missingTranslation.targetLanguage,
                isLanguageDetected = missingTranslation.sourceLanguage == null,
                translatedAtMillis = missingTranslation.translatedAtMillis
            )

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

            emit(Resource.Success(domainTranslation))

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