package com.catvasiliy.mydic.presentation.translation_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.use_case.translate.GetTranslationUseCase
import com.catvasiliy.mydic.domain.use_case.translate.TranslateUseCase
import com.catvasiliy.mydic.domain.use_case.translate.UpdateMissingTranslationUseCase
import com.catvasiliy.mydic.domain.util.Resource
import com.catvasiliy.mydic.presentation.model.toLanguage
import com.catvasiliy.mydic.presentation.model.toLanguageNotNull
import com.catvasiliy.mydic.presentation.model.toMissingTranslation
import com.catvasiliy.mydic.presentation.model.toUiTranslation
import com.catvasiliy.mydic.presentation.model.translation.UiLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationDetailsViewModel @Inject constructor(
    private val translateUseCase: TranslateUseCase,
    private val getTranslationUseCase: GetTranslationUseCase,
    private val updateMissingTranslationUseCase: UpdateMissingTranslationUseCase
): ViewModel() {

    private val _state = MutableStateFlow<TranslationDetailsState>(TranslationDetailsState.Loading)
    val state = _state.asStateFlow()

    private var currentJob: Job? = null

    override fun onCleared() {
        currentJob = null
        super.onCleared()
    }

    fun translate(
        sourceText: String,
        sourceLanguage: UiLanguage?,
        targetLanguage: UiLanguage
    ) {
        _state.update { TranslationDetailsState.Loading }

        currentJob?.cancel()
        currentJob = translateUseCase(
            sourceLanguage = sourceLanguage?.toLanguage(),
            targetLanguage = targetLanguage.toLanguageNotNull(),
            sourceText = sourceText
        )
        .onEach(::processResult)
        .launchIn(viewModelScope)
    }

    fun loadTranslation(id: Long, isMissingTranslation: Boolean = false) {
        _state.update { TranslationDetailsState.Loading }

        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            val translation = getTranslationUseCase(id, isMissingTranslation).toUiTranslation()

            val translationState: TranslationDetailsState = if (!translation.isMissingTranslation) {
                TranslationDetailsState.Translation(translation)
            } else {
                TranslationDetailsState.MissingTranslation(translation)
            }

            _state.update { translationState }
        }
    }

    fun updateMissingTranslation() {
        val missingTranslationState = state.value as TranslationDetailsState.MissingTranslation

        _state.update { TranslationDetailsState.Loading }

        currentJob?.cancel()
        currentJob = updateMissingTranslationUseCase(
            missingTranslation = missingTranslationState.missingTranslation.toMissingTranslation()
        )
        .onEach(::processResult)
        .launchIn(viewModelScope)
    }

    private fun processResult(result: Resource<Translation>) {
        _state.update {
            val translation = result.data ?: throw IllegalStateException("No data.")
            when (result) {
                is Resource.Success -> TranslationDetailsState.Translation(translation.toUiTranslation())
                is Resource.Error -> TranslationDetailsState.MissingTranslation(translation.toUiTranslation())
            }
        }
    }
}