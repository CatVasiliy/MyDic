package com.catvasiliy.mydic.presentation.translations_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.Translation
import com.catvasiliy.mydic.domain.use_case.TranslationUseCases
import com.catvasiliy.mydic.presentation.util.TranslationSort
import com.catvasiliy.mydic.presentation.util.sortedCustom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationsListViewModel @Inject constructor(
    private val translationUseCases: TranslationUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(TranslationsListState())
    val state = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<TranslationsListUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var lastDeletedTranslation: Translation? = null

    init {
        loadTranslationsList()
    }

    private fun loadTranslationsList() {
        translationUseCases.getTranslationsList().onEach { translations ->
            _state.value = state.value.copy(
                translations = translations.sortedCustom(state.value.sortInfo)
            )
        }
        .launchIn(viewModelScope)
    }

    fun searchTranslations(searchText: String) {
        val searchResult = if (searchText.isNotBlank()) {
            state.value.translations?.filter { translation ->
                translation.sourceText.contains(searchText)
            }
        } else {
            null
        }
        _state.value = state.value.copy(
            searchResultTranslations = searchResult
        )
    }

    fun sortTranslations(sortInfo: TranslationSort) {
        val sortedTranslations = state.value.translations?.sortedCustom(sortInfo) ?: return
        _state.value = state.value.copy(
            translations = sortedTranslations,
            sortInfo = sortInfo
        )
    }

    fun deleteTranslation(id: Long, isMissingTranslation: Boolean) {
        viewModelScope.launch {
            lastDeletedTranslation = translationUseCases.deleteTranslation(id, isMissingTranslation)
            _eventFlow.emit(TranslationsListUiEvent.ShowUndoDeleteSnackbar)
        }
    }

    fun undoDeleteTranslation() {
        viewModelScope.launch {
            lastDeletedTranslation?.let { translationUseCases.insertTranslation(it) }
            lastDeletedTranslation = null
        }
    }
}