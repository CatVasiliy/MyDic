package com.catvasiliy.mydic.presentation.translations_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.TranslationSortingInfo
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing.GetTranslationOrganizingPreferencesUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing.ResetOrganizingPreferencesUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing.UpdateSourceLanguageFilteringInfoUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing.UpdateTargetLanguageFilteringInfoUseCase
import com.catvasiliy.mydic.domain.use_case.preferences.translation_organizing.UpdateTranslationSortingInfoUseCase
import com.catvasiliy.mydic.domain.use_case.translate.DeleteTranslationUseCase
import com.catvasiliy.mydic.domain.use_case.translate.GetTranslationsListUseCase
import com.catvasiliy.mydic.domain.use_case.translate.InsertTranslationUseCase
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiSourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTargetLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.toSourceLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.toTargetLanguageFilteringInfo
import com.catvasiliy.mydic.presentation.model.toTranslation
import com.catvasiliy.mydic.presentation.model.toUiTranslation
import com.catvasiliy.mydic.presentation.model.toUiTranslationListItem
import com.catvasiliy.mydic.presentation.model.toUiTranslationOrganizingPreferences
import com.catvasiliy.mydic.presentation.model.translation.UiTranslation
import com.catvasiliy.mydic.presentation.translations_list.translation_organizing.filterBySourceLanguage
import com.catvasiliy.mydic.presentation.translations_list.translation_organizing.filterBySourceTextContains
import com.catvasiliy.mydic.presentation.translations_list.translation_organizing.filterByTargetLanguage
import com.catvasiliy.mydic.presentation.translations_list.translation_organizing.sortedCustom
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
    getTranslationOrganizingPreferencesUseCase: GetTranslationOrganizingPreferencesUseCase,
    private val updateTranslationSortingInfoUseCase: UpdateTranslationSortingInfoUseCase,
    private val updateSourceLanguageFilteringInfoUseCase: UpdateSourceLanguageFilteringInfoUseCase,
    private val updateTargetLanguageFilteringInfoUseCase: UpdateTargetLanguageFilteringInfoUseCase,
    private val resetOrganizingPreferencesUseCase: ResetOrganizingPreferencesUseCase
) : ViewModel() {

    private val _translationsList = getTranslationsListUseCase().map { domainTranslations ->
        domainTranslations.map(Translation::toUiTranslationListItem)
    }

    private val _translationOrganizingPreferences = getTranslationOrganizingPreferencesUseCase()
        .map { domainOrganizingPreferences ->
            domainOrganizingPreferences.toUiTranslationOrganizingPreferences()
        }

    private val _searchQuery = MutableStateFlow("")

    private val _lastDeletedTranslation = MutableStateFlow<UiTranslation?>(null)

    val state: StateFlow<TranslationsListState> = combine(
        _translationsList,
        _translationOrganizingPreferences,
        _searchQuery,
        _lastDeletedTranslation
    ) { translationsList, organizingPreferences, searchQuery, lastDeletedTranslation ->

        val resultTranslationsList = translationsList
            .filterBySourceTextContains(searchQuery)
            .filterBySourceLanguage(organizingPreferences.sourceLanguageFilteringInfo)
            .filterByTargetLanguage(organizingPreferences.targetLanguageFilteringInfo)
            .sortedCustom(organizingPreferences.sortingInfo)

        TranslationsListState(
            translations = resultTranslationsList,
            organizingPreferences = organizingPreferences,
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

    private var searchJob: Job? = null

    private val searchDelayMillis: Long = 300

    override fun onCleared() {
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

    fun filterTranslationsBySourceLanguage(filteringInfo: UiSourceLanguageFilteringInfo) {
        viewModelScope.launch {
            updateSourceLanguageFilteringInfoUseCase(filteringInfo.toSourceLanguageFilteringInfo())
        }
    }

    fun filterTranslationsByTargetLanguage(filteringInfo: UiTargetLanguageFilteringInfo) {
        viewModelScope.launch {
            updateTargetLanguageFilteringInfoUseCase(filteringInfo.toTargetLanguageFilteringInfo())
        }
    }

    fun resetOrganizingPreferences() {
        viewModelScope.launch {
            resetOrganizingPreferencesUseCase()
        }
    }

    fun removeTranslation(id: Long, isMissingTranslation: Boolean) {
        viewModelScope.launch {
            _lastDeletedTranslation.update {
                deleteTranslationUseCase(id, isMissingTranslation).toUiTranslation()
            }
            _eventFlow.emit(TranslationsListUiEvent.ShowUndoDeleteSnackbar)
        }
    }

    fun undoRemoveTranslation() {
        val lastDeletedTranslation = _lastDeletedTranslation.value ?: return
        viewModelScope.launch {
            insertTranslationUseCase(lastDeletedTranslation.toTranslation())
            _lastDeletedTranslation.update { null }
        }
    }
}