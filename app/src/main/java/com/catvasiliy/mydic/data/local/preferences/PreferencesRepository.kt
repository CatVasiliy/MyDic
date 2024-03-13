package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.DataStore
import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.domain.model.preferences.TranslationPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.TranslationOrganizingPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingInterval
import com.catvasiliy.mydic.domain.model.preferences.translation_sending.TranslationSendingPreferences
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.SourceLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.filtering.TargetLanguageFilteringInfo
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.TranslationSortingInfo
import com.catvasiliy.mydic.domain.model.translation.language.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferences: DataStore<TranslationPreferences>
) {

    suspend fun updateDefaultSourceLanguage(sourceLanguage: Language?) {
        preferences.updateData { translationPreferences ->
            val languagePreferences = translationPreferences.languagePreferences.copy(
                defaultSourceLanguage = sourceLanguage
            )
            translationPreferences.copy(languagePreferences = languagePreferences)
        }
    }

    suspend fun updateDefaultTargetLanguage(targetLanguage: Language) {
        preferences.updateData { translationPreferences ->
            val languagePreferences = translationPreferences.languagePreferences.copy(
                defaultTargetLanguage = targetLanguage
            )
            translationPreferences.copy(languagePreferences = languagePreferences)
        }
    }

    fun getLanguagePreferences(): Flow<LanguagePreferences> {
        return preferences.data.map { translationPreferences ->
            translationPreferences.languagePreferences
        }
    }

    suspend fun updateTranslationSortingInfo(sortingInfo: TranslationSortingInfo) {
        preferences.updateData { translationPreferences ->
            val organizingPreferences = translationPreferences.organizingPreferences.copy(
                sortingInfo = sortingInfo
            )
            translationPreferences.copy(organizingPreferences = organizingPreferences)
        }
    }

    suspend fun updateSourceLanguageFilteringInfo(filteringInfo: SourceLanguageFilteringInfo) {
        preferences.updateData { translationPreferences ->
            val organizingPreferences = translationPreferences.organizingPreferences.copy(
                sourceLanguageFilteringInfo = filteringInfo
            )
            translationPreferences.copy(organizingPreferences = organizingPreferences)
        }
    }

    suspend fun updateTargetLanguageFilteringInfo(filteringInfo: TargetLanguageFilteringInfo) {
        preferences.updateData { translationPreferences ->
            val organizingPreferences = translationPreferences.organizingPreferences.copy(
                targetLanguageFilteringInfo = filteringInfo
            )
            translationPreferences.copy(organizingPreferences = organizingPreferences)
        }
    }

    fun getTranslationOrganizingPreferences(): Flow<TranslationOrganizingPreferences> {
        return preferences.data.map { translationPreferences ->
            translationPreferences.organizingPreferences
        }
    }

    suspend fun updateIsTranslationSendingEnabled(isSendingEnabled: Boolean) {
        preferences.updateData { translationPreferences ->
            val translationSendingPreferences = translationPreferences.translationSendingPreferences.copy(
                isSendingEnabled = isSendingEnabled
            )
            translationPreferences.copy(translationSendingPreferences = translationSendingPreferences)
        }
    }

    suspend fun updateTranslationSendingInterval(interval: TranslationSendingInterval) {
        preferences.updateData { translationPreferences ->
            val translationSendingPreferences = translationPreferences.translationSendingPreferences.copy(
                sendingInterval = interval
            )
            translationPreferences.copy(translationSendingPreferences = translationSendingPreferences)
        }
    }

    fun getTranslationSendingPreferences(): Flow<TranslationSendingPreferences> {
        return preferences.data.map { translationPreferences ->
            translationPreferences.translationSendingPreferences
        }
    }
}