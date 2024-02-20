package com.catvasiliy.mydic.presentation.translation_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.translation.MissingTranslation
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.use_case.translate.GetTranslationUseCase
import com.catvasiliy.mydic.domain.use_case.translate.TranslateUseCase
import com.catvasiliy.mydic.domain.use_case.translate.UpdateMissingTranslationUseCase
import com.catvasiliy.mydic.domain.util.Resource
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

    private val _state = MutableStateFlow(TranslationDetailsState())
    val state = _state.asStateFlow()

    private var currentJob: Job? = null

    override fun onCleared() {
        currentJob = null
        super.onCleared()
    }

    fun translate(sourceText: String) {
        currentJob?.cancel()
        currentJob = translateUseCase(
            sourceLanguage = "en",
            targetLanguage = "ru",
            sourceText = sourceText
        )
        .onEach { processResult(it) }
        .launchIn(viewModelScope)
    }

    private fun processResult(result: Resource<Translation>) {
        val newState = when(result) {
            is Resource.Loading -> {
                state.value.copy(
                    translation = result.data,
                    isLoading = true
                )
            }
            is Resource.Success -> {
                state.value.copy(
                    isLoading = false
                )
            }
            is Resource.Error -> {
                state.value.copy(
                    translation = result.data,
                    isLoading = false,
                    errorMessage = result.message ?: "Something went wrong!"
                )
            }
        }
        _state.update { newState }
    }

    fun loadTranslation(id: Long, isMissingTranslation: Boolean = false) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _state.update {
                state.value.copy(
                    translation = getTranslationUseCase(id, isMissingTranslation)
                )
            }
        }
    }

    fun updateMissingTranslation() {
        val translation = state.value.translation ?: return
        if (translation !is MissingTranslation)
            throw IllegalStateException("Translation is not a MissingTranslation")

        currentJob?.cancel()
        currentJob = updateMissingTranslationUseCase(
            sourceLanguage = "en",
            targetLanguage = "ru",
            missingTranslation = translation
        )
        .onEach { processResult(it) }
        .launchIn(viewModelScope)
    }
}