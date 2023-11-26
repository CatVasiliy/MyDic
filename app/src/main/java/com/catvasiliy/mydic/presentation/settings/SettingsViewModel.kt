package com.catvasiliy.mydic.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.data.local.preferences.PreferencesRepository
import com.catvasiliy.mydic.domain.model.settings.Period
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: PreferencesRepository
) : ViewModel() {

    val state = preferences.getPreferences()
        .map { sendTranslationPreferences ->
            SettingsState(
                sendTranslationPreferences = sendTranslationPreferences
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsState()
        )

    fun setIsSendingEnabled(isSendingEnabled: Boolean) {
        viewModelScope.launch {
            preferences.setIsSendingEnabled(isSendingEnabled)
        }
    }

    fun setPeriod(period: Period) {
        viewModelScope.launch {
            preferences.setPeriod(period)
        }
    }
}