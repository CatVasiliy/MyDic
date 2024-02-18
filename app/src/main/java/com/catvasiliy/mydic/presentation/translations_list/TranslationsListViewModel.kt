package com.catvasiliy.mydic.presentation.translations_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.use_case.translate.DeleteTranslationUseCase
import com.catvasiliy.mydic.domain.use_case.translate.GetTranslationsListUseCase
import com.catvasiliy.mydic.domain.use_case.translate.InsertTranslationUseCase
import com.catvasiliy.mydic.presentation.util.SortType
import com.catvasiliy.mydic.presentation.util.TranslationSort
import com.catvasiliy.mydic.presentation.util.sortedCustom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationsListViewModel @Inject constructor(
    getTranslationsList: GetTranslationsListUseCase,
    private val insertTranslation: InsertTranslationUseCase,
    private val deleteTranslation: DeleteTranslationUseCase
) : ViewModel() {

    private val _translationsList = getTranslationsList()

    private val _sortInfo = MutableStateFlow<TranslationSort>(TranslationSort.Date(SortType.Descending))

    private val _searchQuery = MutableStateFlow("")

    private val _lastDeletedTranslation = MutableStateFlow<Translation?>(null)

    val state: StateFlow<TranslationsListState> = combine(
        _translationsList,
        _sortInfo,
        _searchQuery,
        _lastDeletedTranslation
    ) { translationsList, sortInfo, searchQuery, lastDeletedTranslation ->

        val resultTranslationsList = if (searchQuery.isNotBlank()) {
            translationsList.filter { it.sourceText.contains(searchQuery) }
        } else {
            translationsList
        }.sortedCustom(sortInfo)

        TranslationsListState(
            translations = resultTranslationsList,
            sortInfo = sortInfo,
            searchQuery = searchQuery,
            lastDeletedTranslation = lastDeletedTranslation
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TranslationsListState()
    )

    private val _eventFlow = MutableSharedFlow<TranslationsListUiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentJob: Job? = null
    private var searchJob: Job? = null

    private val searchDelayMillis: Long = 300

    override fun onCleared() {
        currentJob = null
        searchJob = null
        super.onCleared()
    }

    fun searchTranslations(searchQuery: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            if (searchQuery.isNotBlank()) {
                delay(searchDelayMillis)
            }
            _searchQuery.update { searchQuery }
        }
    }

    fun sortTranslations(sortInfo: TranslationSort) {
        _sortInfo.update { sortInfo }
    }

    fun removeTranslation(id: Long, isMissingTranslation: Boolean) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _lastDeletedTranslation.update {
                deleteTranslation(id, isMissingTranslation)
            }
            _eventFlow.emit(TranslationsListUiEvent.ShowUndoDeleteSnackbar)
        }
    }

    fun undoRemoveTranslation() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _lastDeletedTranslation.value?.let { insertTranslation(it) }
            _lastDeletedTranslation.update { null }
        }
    }
}