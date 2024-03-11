package com.catvasiliy.mydic.presentation.translations_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.preferences.translation_sorting.TranslationSortingInfo
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.use_case.preferences.translation_sorting.GetTranslationSortingInfoUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_sorting.UpdateTranslationSortingInfoUseCase
import com.catvasiliy.mydic.domain.use_case.translate.DeleteTranslationUseCase
import com.catvasiliy.mydic.domain.use_case.translate.GetTranslationsListUseCase
import com.catvasiliy.mydic.domain.use_case.translate.InsertTranslationUseCase
import com.catvasiliy.mydic.presentation.model.toTranslation
import com.catvasiliy.mydic.presentation.model.toUiTranslation
import com.catvasiliy.mydic.presentation.model.toUiTranslationListItem
import com.catvasiliy.mydic.presentation.model.translation.SourceLanguageFilterInfo
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.util.filterBySourceLanguage
import com.catvasiliy.mydic.presentation.util.filterBySourceTextContains
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TranslationsListViewModel @Inject constructor(
    getTranslationsListUseCase: GetTranslationsListUseCase,
    private val insertTranslationUseCase: InsertTranslationUseCase,
    private val deleteTranslationUseCase: DeleteTranslationUseCase,
    getTranslationSortingInfoUseCase: GetTranslationSortingInfoUseCase,
    private val updateTranslationSortingInfoUseCase: UpdateTranslationSortingInfoUseCase
) : ViewModel() {

    private val _translationsList = getTranslationsListUseCase().map { domainTranslations ->
        domainTranslations.map(Translation::toUiTranslationListItem)
    }

    private val _sortingInfo = getTranslationSortingInfoUseCase()

    private val _filterInfo = MutableStateFlow<SourceLanguageFilterInfo?>(null)

    private val _searchQuery = MutableStateFlow("")

    private val _lastDeletedTranslation = MutableStateFlow<UiTranslation?>(null)

    val state: StateFlow<TranslationsListState> = combine(
        _translationsList,
        _sortingInfo,
        _filterInfo,
        _searchQuery,
        _lastDeletedTranslation
    ) { translationsList, sortingInfo, filterInfo, searchQuery, lastDeletedTranslation ->

        val resultTranslationsList = translationsList
            .filterBySourceTextContains(searchQuery)
            .filterBySourceLanguage(filterInfo)
            .sortedCustom(sortingInfo)

        TranslationsListState(
            translations = resultTranslationsList,
            sortingInfo = sortingInfo,
            filterInfo = filterInfo,
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

    fun sortTranslations(sortingInfo: TranslationSortingInfo) {
        viewModelScope.launch {
            updateTranslationSortingInfoUseCase(sortingInfo)
        }
    }

    fun filterTranslations(sourceLanguageFilterInfo: SourceLanguageFilterInfo?) {
        _filterInfo.update { sourceLanguageFilterInfo }
    }

    fun removeTranslation(id: Long, isMissingTranslation: Boolean) {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _lastDeletedTranslation.update {
                deleteTranslationUseCase(id, isMissingTranslation).toUiTranslation()
            }
            _eventFlow.emit(TranslationsListUiEvent.ShowUndoDeleteSnackbar)
        }
    }

    fun undoRemoveTranslation() {
        currentJob?.cancel()
        currentJob = viewModelScope.launch {
            _lastDeletedTranslation.value?.let { insertTranslationUseCase(it.toTranslation()) }
            _lastDeletedTranslation.update { null }
        }
    }
}