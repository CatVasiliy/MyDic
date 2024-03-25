package com.catvasiliy.mydic.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.use_case.preferences.translation_sending.GetTranslationSendingPreferencesUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_sending.ToggleTranslationSendingUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_sending.UpdateTranslationSendingIntervalUseCase
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingInterval
import com.catvasiliy.mydic.presentation.model.preferences.translation_sending.UiTranslationSendingPreferences
import com.catvasiliy.mydic.presentation.model.toTranslationSendingInterval
import com.catvasiliy.mydic.presentation.model.toTranslationSendingPreferences
import com.catvasiliy.mydic.presentation.model.toUiTranslationSendingPreferences
import com.catvasiliy.mydic.presentation.settings.translation_sending.TranslationSendingAlarmScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getTranslationSendingPreferencesUseCase: GetTranslationSendingPreferencesUseCase,
    private val toggleTranslationSendingUseCase: ToggleTranslationSendingUseCase,
    private val updateTranslationSendingIntervalUseCase: UpdateTranslationSendingIntervalUseCase,
    private val alarmScheduler: TranslationSendingAlarmScheduler
) : ViewModel() {

    val state = getTranslationSendingPreferencesUseCase()
        .map { translationSendingPreferences ->
            SettingsState(
                sendingPreferences = translationSendingPreferences.toUiTranslationSendingPreferences()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsState()
        )

    fun toggleTranslationSending(
        isSendingEnabled: Boolean,
        sendingInterval: UiTranslationSendingInterval
    ) {
        viewModelScope.launch {
            if (isSendingEnabled) {
                alarmScheduler.schedule(sendingInterval.toTranslationSendingInterval())
            } else {
                alarmScheduler.cancel()
            }

            val sendingPreferences = UiTranslationSendingPreferences(
                isSendingEnabled = isSendingEnabled,
                sendingInterval = sendingInterval
            )

            toggleTranslationSendingUseCase(sendingPreferences.toTranslationSendingPreferences())
        }
    }

    fun updateTranslationSendingInterval(interval: UiTranslationSendingInterval) {
        viewModelScope.launch {
            updateTranslationSendingIntervalUseCase(interval.toTranslationSendingInterval())
        }
    }
}