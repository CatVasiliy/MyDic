package com.catvasiliy.mydic.presentation.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.use_case.preferences.translation_language.GetLanguagePreferencesUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_language.UpdateDefaultSourceLanguageUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_language.UpdateDefaultTargetLanguageUseCase
import com.catvasiliy.mydic.presentation.model.toLanguage
import com.catvasiliy.mydic.presentation.model.toLanguageNotNull
import com.catvasiliy.mydic.presentation.model.toUiLanguagePreferences
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
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
                languagePreferences = languagePreferences.toUiLanguagePreferences()
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TranslateState()
        )

    fun updateDefaultSourceLanguage(sourceLanguage: UiLanguage?) {
        viewModelScope.launch {
            updateDefaultSourceLanguageUseCase(sourceLanguage?.toLanguage())
        }
    }

    fun updateDefaultTargetLanguage(targetLanguage: UiLanguage) {
        viewModelScope.launch {
            updateDefaultTargetLanguageUseCase(targetLanguage.toLanguageNotNull())
        }
    }
}