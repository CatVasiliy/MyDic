package com.catvasiliy.mydic.presentation.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.translation.Language
import com.catvasiliy.mydic.domain.use_case.settings.GetLanguagePreferencesUseCase
import com.catvasiliy.mydic.domain.use_case.settings.UpdateDefaultSourceLanguageUseCase
import com.catvasiliy.mydic.domain.use_case.settings.UpdateDefaultTargetLanguageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslateViewModel @Inject constructor(
    getLanguagePreferencesUseCase: GetLanguagePreferencesUseCase,
    private val updateDefaultSourceLanguageUseCase: UpdateDefaultSourceLanguageUseCase,
    private val updateDefaultTargetLanguageUseCase: UpdateDefaultTargetLanguageUseCase
) : ViewModel() {

    val state = getLanguagePreferencesUseCase()
        .map { languagePreferences ->
            TranslateState(
                languagePreferences = languagePreferences
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TranslateState()
        )

    fun updateDefaultSourceLanguage(sourceLanguage: Language) {
        viewModelScope.launch {
            updateDefaultSourceLanguageUseCase(sourceLanguage)
        }
    }

    fun updateDefaultTargetLanguage(targetLanguage: Language) {
        viewModelScope.launch {
            updateDefaultTargetLanguageUseCase(targetLanguage)
        }
    }
}