package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.DataStore
import com.catvasiliy.mydic.domain.model.preferences.LanguagePreferences
import com.catvasiliy.mydic.domain.model.preferences.TranslationPreferences
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingInterval
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingPreferences
import com.catvasiliy.mydic.domain.model.translation.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferences: DataStore<TranslationPreferences>
) {

    suspend fun updateDefaultSourceLanguage(sourceLanguage: Language) {
        preferences.updateData { preferencesData ->
            val languagePreferences = preferencesData.languagePreferences.copy(
                defaultSourceLanguage = sourceLanguage
            )
            preferencesData.copy(
                languagePreferences = languagePreferences
            )
        }
    }

    suspend fun updateDefaultTargetLanguage(targetLanguage: Language) {
        preferences.updateData { preferencesData ->
            val languagePreferences = preferencesData.languagePreferences.copy(
                defaultTargetLanguage = targetLanguage
            )
            preferencesData.copy(
                languagePreferences = languagePreferences
            )
        }
    }

    fun getLanguagePreferences(): Flow<LanguagePreferences> {
        return preferences.data.map { translationPreferences ->
            translationPreferences.languagePreferences
        }
    }

    suspend fun updateIsTranslationSendingEnabled(isSendingEnabled: Boolean) {
        preferences.updateData { preferencesData ->
            val translationSendingPreferences = preferencesData.translationSendingPreferences.copy(
                isSendingEnabled = isSendingEnabled
            )
            preferencesData.copy(
                translationSendingPreferences = translationSendingPreferences
            )
        }
    }

    suspend fun updateTranslationSendingInterval(interval: TranslationSendingInterval) {
        preferences.updateData { preferencesData ->
            val translationSendingPreferences = preferencesData.translationSendingPreferences.copy(
                sendingInterval = interval
            )
            preferencesData.copy(
                translationSendingPreferences = translationSendingPreferences
            )
        }
    }

    fun getTranslationSendingPreferences(): Flow<TranslationSendingPreferences> {
        return preferences.data.map { translationPreferences ->
            translationPreferences.translationSendingPreferences
        }
    }
}