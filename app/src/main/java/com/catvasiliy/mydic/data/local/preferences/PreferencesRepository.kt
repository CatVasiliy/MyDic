package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.DataStore
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingInterval
import com.catvasiliy.mydic.domain.model.preferences.TranslationSendingPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferences: DataStore<TranslationSendingPreferences>
) {

    suspend fun setIsTranslationSendingEnabled(isSendingEnabled: Boolean) {
        preferences.updateData { preferencesData ->
            preferencesData.copy(
                isSendingEnabled = isSendingEnabled
            )
        }
    }

    suspend fun setTranslationSendingInterval(interval: TranslationSendingInterval) {
        preferences.updateData { preferencesData ->
            preferencesData.copy(
                sendingInterval = interval
            )
        }
    }

    fun getPreferences(): Flow<TranslationSendingPreferences> {
        return preferences.data
    }
}