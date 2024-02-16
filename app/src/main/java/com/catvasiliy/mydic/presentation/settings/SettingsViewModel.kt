package com.catvasiliy.mydic.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.catvasiliy.mydic.domain.model.settings.TranslationSendingInterval
import com.catvasiliy.mydic.domain.model.settings.TranslationSendingPreferences
import com.catvasiliy.mydic.domain.use_case.settings.GetPreferencesUseCase
import com.catvasiliy.mydic.domain.use_case.settings.UpdateTranslationSendingIntervalUseCase
import com.catvasiliy.mydic.domain.use_case.settings.ToggleTranslationSendingUseCase
import com.catvasiliy.mydic.presentation.settings.translation_sending.TranslationNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getPreferences: GetPreferencesUseCase,
    private val toggleTranslationSending: ToggleTranslationSendingUseCase,
    private val updateTranslationSendingInterval: UpdateTranslationSendingIntervalUseCase,
    private val workManager: WorkManager
) : ViewModel() {

    val state = getPreferences()
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

    fun toggleTranslationSending(
        isSendingEnabled: Boolean,
        sendingInterval: TranslationSendingInterval
    ) {
        viewModelScope.launch {

            val workRequest = PeriodicWorkRequestBuilder<TranslationNotificationWorker>(
                repeatInterval = sendingInterval.duration,
                repeatIntervalTimeUnit = sendingInterval.timeUnit
            )
            .setInitialDelay(
                duration = sendingInterval.duration,
                timeUnit = sendingInterval.timeUnit
            )
            .build()

            if (isSendingEnabled) {
                workManager.enqueueUniquePeriodicWork(
                    TranslationNotificationWorker.UNIQUE_WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    workRequest
                )
            } else {
                workManager.cancelUniqueWork(TranslationNotificationWorker.UNIQUE_WORK_NAME)
            }

            toggleTranslationSending(
                TranslationSendingPreferences(
                    isSendingEnabled = isSendingEnabled,
                    sendingInterval = sendingInterval
                )
            )
        }
    }

    fun setTranslationSendingInterval(interval: TranslationSendingInterval) {
        viewModelScope.launch {
            updateTranslationSendingInterval(interval)
        }
    }
}