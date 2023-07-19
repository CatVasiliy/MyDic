package com.catvasiliy.mydic.presentation.translation_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.Translation
import com.catvasiliy.mydic.domain.use_case.TranslationUseCases
import com.catvasiliy.mydic.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationDetailsViewModel @Inject constructor(
    private val translationUseCases: TranslationUseCases
): ViewModel() {

    private val _state = MutableStateFlow(TranslationDetailsState())
    val state = _state.asStateFlow()

    private var translateJob: Job? = null

    fun translate(sourceText: String) {
        translateJob?.cancel()
        translateJob = translationUseCases.getTranslation(
            sourceLanguage = "en",
            targetLanguage = "ru",
            sourceText = sourceText
        ).onEach { processResult(it) }.launchIn(viewModelScope)
    }

    private fun processResult(result: Resource<Translation>) {
        when(result) {
            is Resource.Loading -> {
                _state.value = state.value.copy(
                    translation = result.data,
                    isLoading = true
                )
            }
            is Resource.Success -> {
                _state.value = state.value.copy(
                    isLoading = false
                )
            }
            is Resource.Error -> {
                _state.value = state.value.copy(
                    translation = result.data,
                    isLoading = false,
                    errorMessage = result.message ?: "Something went wrong!"
                )
            }
        }
    }

    fun loadTranslation(id: Long, isMissingTranslation: Boolean = false) {
        viewModelScope.launch {
            _state.value = state.value.copy(
                translation = translationUseCases.getTranslation(id, isMissingTranslation)
            )
        }
    }

    fun updateMissingTranslation() {
        val missingTranslation = state.value.translation ?: return
        translateJob?.cancel()
        translateJob = translationUseCases.getTranslation(
            sourceLanguage = "en",
            targetLanguage = "ru",
            translation = missingTranslation
        ).onEach { processResult(it) }.launchIn(viewModelScope)
    }
}