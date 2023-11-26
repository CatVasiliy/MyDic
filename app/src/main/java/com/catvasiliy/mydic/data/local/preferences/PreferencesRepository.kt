package com.catvasiliy.mydic.data.local.preferences

import androidx.datastore.core.DataStore
import com.catvasiliy.mydic.domain.model.settings.Period
import com.catvasiliy.mydic.domain.model.settings.SendTranslationPreferences
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepository @Inject constructor(
    private val preferences: DataStore<SendTranslationPreferences>
) {

    suspend fun setIsSendingEnabled(isSendingEnabled: Boolean) {
        preferences.updateData { preferencesData ->
            preferencesData.copy(
                isSendingEnabled = isSendingEnabled
            )
        }
    }

    suspend fun setPeriod(period: Period) {
        preferences.updateData { preferencesData ->
            preferencesData.copy(
                period = period
            )
        }
    }

    fun getPreferences(): Flow<SendTranslationPreferences> {
        return preferences.data
    }
}