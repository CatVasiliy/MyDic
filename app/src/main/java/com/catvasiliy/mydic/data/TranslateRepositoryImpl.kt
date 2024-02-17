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
import com.catvasiliy.mydic.domain.model.settings.TranslationForSending
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.repository.TranslateRepository
import com.catvasiliy.mydic.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class TranslateRepositoryImpl @Inject constructor(
    private val translateApi: TranslateApi,
    private val translationDao: TranslationDao
) : TranslateRepository {

    override fun getTranslationFromApi(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String
    ): Flow<Resource<Translation>> = baseTranslationFlow(
        sourceLanguage,
        targetLanguage,
        sourceText
    ).onCompletion {exception ->
        if (exception != null) return@onCompletion

        emit(Resource.Success())
    }.catch {exception ->
        exception.printStackTrace()
        val missingTranslation = MissingTranslation.fromSourceText(sourceText)
        val errorResource = getErrorResource(
            exception as Exception,
            missingTranslation,
            true
        )
        emit(errorResource)
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
        translationDao.deleteTranslationForSendingById(id)
        translationDao.deleteTranslationById(id)
    }

    override fun updateMissingTranslationFromApi(
        sourceLanguage: String,
        targetLanguage: String,
        missingTranslation: MissingTranslation
    ): Flow<Resource<Translation>> = baseTranslationFlow(
        sourceLanguage,
        targetLanguage,
        missingTranslation.sourceText
    ).onCompletion { exception ->
        if (exception != null) return@onCompletion

        translationDao.deleteMissingTranslationById(missingTranslation.id)
        emit(Resource.Success())
    }.catch { exception ->
        val errorResource = getErrorResource(
            exception as Exception,
            missingTranslation,
            false
        )
        emit(errorResource)
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

    private fun baseTranslationFlow(
        sourceLanguage: String,
        targetLanguage: String,
        sourceText: String
    ): Flow<Resource<Translation>> = flow {

        emit(Resource.Loading())

        try {
            val domainTranslation = translateApi.getTranslation(
                sourceLanguage,
                targetLanguage,
                sourceText
            ).toExtendedTranslation()

            emit(Resource.Loading(domainTranslation))

            val cachedTranslation = domainTranslation.toCachedTranslation()
            translationDao.insertTranslation(cachedTranslation)

        } catch (e: Exception) {
            throw e
        }
    }

    private suspend fun getErrorResource(
        exception: Exception,
        missingTranslation: MissingTranslation,
        isNewMissingTranslation: Boolean
    ): Resource.Error<Translation> {
        val resourceMissingTranslation = if (isNewMissingTranslation) {
            val cachedMissingTranslation = missingTranslation.toCachedMissingTranslation()
            val id = translationDao.insertMissingTranslation(cachedMissingTranslation)
            getMissingTranslationById(id)
        } else {
            missingTranslation
        }
        return Resource.Error(exception.localizedMessage, resourceMissingTranslation)
    }
}