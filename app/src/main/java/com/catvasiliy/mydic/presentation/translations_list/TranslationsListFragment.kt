package com.catvasiliy.mydic.presentation.translations_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.IdRes
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.BottomSheetOrganizeBinding
import com.catvasiliy.mydic.databinding.FragmentTranslationsListBinding
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.SortingOrder
import com.catvasiliy.mydic.domain.model.preferences.translation_organizing.sorting.TranslationSortingInfo
import com.catvasiliy.mydic.presentation.MainActivity
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTranslationOrganizingPreferences
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
import com.catvasiliy.mydic.presentation.translations_list.spinner.SourceLanguageFilterSpinnerAdapter
import com.catvasiliy.mydic.presentation.translations_list.spinner.SourceLanguageFilterSpinnerItem
import com.catvasiliy.mydic.presentation.translations_list.spinner.TargetLanguageFilterSpinnerAdapter
import com.catvasiliy.mydic.presentation.translations_list.spinner.TargetLanguageFilterSpinnerItem
import com.catvasiliy.mydic.presentation.util.checkWithTag
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.setSelectionWithTag
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslationsListFragment : Fragment() {

    private var _binding: FragmentTranslationsListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationsListViewModel by viewModels()

    private var _bottomSheetOrganizeBinding: BottomSheetOrganizeBinding? = null
    private val bottomSheetOrganizeBinding get() = _bottomSheetOrganizeBinding!!

    private val translationsListAdapter = TranslationsListAdapter()

    private val translationsListScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val isShown = binding.fabTranslate.isShown
            if (dy > 0 && isShown) {
                binding.fabTranslate.hide()
            } else if(dy < 0 && !isShown) {
                binding.fabTranslate.show()
            }
        }
    }

    private val swipeToDeleteCallback by lazy(LazyThreadSafetyMode.NONE) {
        object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                if (direction != ItemTouchHelper.LEFT) {
                    return
                }
                val itemPosition = viewHolder.adapterPosition
                val translationsList = viewModel.state.value.translations

                val id = translationsList[itemPosition].id
                val isMissingTranslation = translationsList[itemPosition].isMissingTranslation
                viewModel.removeTranslation(id, isMissingTranslation)
            }
        }
    }

    private val bottomSheetOrganize by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext()).apply {
            setContentView(bottomSheetOrganizeBinding.root)
        }
    }

    private val slFilterAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SourceLanguageFilterSpinnerAdapter(requireContext())
    }
    private val slItemSelectedListener = object : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (bottomSheetOrganizeBinding.spSourceLanguage.tag == position) return

            val spinnerItem = parent.getItemAtPosition(position) as SourceLanguageFilterSpinnerItem
            val newSourceLanguageFilteringInfo = spinnerItem.filteringInfo
            viewModel.filterTranslationsBySourceLanguage(newSourceLanguageFilteringInfo)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
    }

    private val tlFilterAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TargetLanguageFilterSpinnerAdapter(requireContext())
    }
    private val tlItemSelectedListener = object : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (bottomSheetOrganizeBinding.spTargetLanguage.tag == position) return

            val spinnerItem = parent.getItemAtPosition(position) as TargetLanguageFilterSpinnerItem
            val newTargetLanguageFilteringInfo = spinnerItem.filteringInfo
            viewModel.filterTranslationsByTargetLanguage(newTargetLanguageFilteringInfo)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationsListBinding.inflate(inflater, container, false)
        _bottomSheetOrganizeBinding = BottomSheetOrganizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    updateOrganizingPreferences(state.organizingPreferences)
                    updateTranslations(state.translations)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collectLatest { event ->
                    onEvent(event)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _bottomSheetOrganizeBinding = null
        _binding = null
    }

    private fun setupView() {
        binding.tbTranslations.setNavigationOnClickListener {
            (requireActivity() as MainActivity).openNavigationDrawer()
        }

        binding.tbTranslations.menu.findItem(R.id.miOrganize).setOnMenuItemClickListener {
            bottomSheetOrganize.show()
            true
        }

        setupSearch()
        setupBottomSheetOrganize()

        val swipeHelper = ItemTouchHelper(swipeToDeleteCallback)

        binding.rvTranslations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = translationsListAdapter
            addOnScrollListener(translationsListScrollListener)
            swipeHelper.attachToRecyclerView(this)
        }

        binding.fabTranslate.setOnClickListener {
            val action = TranslationsListFragmentDirections.openTranslate()
            findNavController().navigate(action)
        }
    }

    private fun setupSearch() {
        val searchView = binding.tbTranslations.menu.findItem(R.id.miSearch).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.searchTranslations(newText)
                return true
            }
        })
    }

    private fun setupBottomSheetOrganize() {

        bottomSheetOrganizeBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (bottomSheetOrganizeBinding.radioGroup.tag == checkedId) return@setOnCheckedChangeListener

            val sortingOrder = viewModel.state.value.organizingPreferences.sortingInfo.sortingOrder
            val newSortingInfo = when (checkedId) {
                R.id.rbDate -> TranslationSortingInfo.Date(sortingOrder)
                R.id.rbSourceText -> TranslationSortingInfo.SourceText(sortingOrder)
                R.id.rbTranslationText -> TranslationSortingInfo.TranslationText(sortingOrder)
                else -> return@setOnCheckedChangeListener
            }

            viewModel.sortTranslations(newSortingInfo)
        }

        bottomSheetOrganizeBinding.cbDescending.setOnCheckedChangeListener { _, isChecked ->
            val currentSortingInfo = viewModel.state.value.organizingPreferences.sortingInfo
            val newSortingOrder = if (isChecked) SortingOrder.Descending else SortingOrder.Ascending
            val currentSortingOrder = currentSortingInfo.sortingOrder
            if (newSortingOrder != currentSortingOrder) {
                val sortingInfo = currentSortingInfo.copyChangeSortingOrder(newSortingOrder)
                viewModel.sortTranslations(sortingInfo)
            }
        }

        bottomSheetOrganizeBinding.spSourceLanguage.apply {
            adapter = slFilterAdapter
            onItemSelectedListener = slItemSelectedListener
        }

        bottomSheetOrganizeBinding.spTargetLanguage.apply {
            adapter = tlFilterAdapter
            onItemSelectedListener = tlItemSelectedListener
        }

        bottomSheetOrganizeBinding.btnRestoreDefault.setOnClickListener {
            viewModel.restoreDefaultOrganizingPreferences()
        }
    }

    private fun updateTranslations(translationsList: List<UiTranslationListItem>) {
        if (translationsList.isNotEmpty()) {
            translationsListAdapter.submitList(translationsList)
            binding.llNoTranslations.hideAndShowOther(binding.llTranslations)
        } else {
            binding.llTranslations.hideAndShowOther(binding.llNoTranslations)
        }
    }

    private fun updateOrganizingPreferences(
        organizingPreferences: UiTranslationOrganizingPreferences
    ) {
        updateSorting(organizingPreferences.sortingInfo)

        val slFilteringPosition = slFilterAdapter.getPosition(
            organizingPreferences.sourceLanguageFilteringInfo
        )

        val tlFilteringPosition = tlFilterAdapter.getPosition(
            organizingPreferences.targetLanguageFilteringInfo
        )
        bottomSheetOrganizeBinding.spSourceLanguage.setSelectionWithTag(slFilteringPosition)
        bottomSheetOrganizeBinding.spTargetLanguage.setSelectionWithTag(tlFilteringPosition)

        bottomSheetOrganizeBinding.btnRestoreDefault.isEnabled = organizingPreferences.isDefault.not()
    }

    private fun updateSorting(sortingInfo: TranslationSortingInfo) {
        val isDescending = sortingInfo.sortingOrder == SortingOrder.Descending
        bottomSheetOrganizeBinding.cbDescending.isChecked = isDescending

        @IdRes
        val idToCheck = when (sortingInfo) {
            is TranslationSortingInfo.Date -> R.id.rbDate
            is TranslationSortingInfo.SourceText -> R.id.rbSourceText
            is TranslationSortingInfo.TranslationText -> R.id.rbTranslationText
        }
        bottomSheetOrganizeBinding.radioGroup.checkWithTag(idToCheck)
    }

    private fun onEvent(event: TranslationsListUiEvent) {
        when (event) {
            is TranslationsListUiEvent.ShowUndoDeleteSnackbar -> showUndoSnackbar()
        }
    }

    private fun showUndoSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.delete_translation_snackbar,
            Snackbar.LENGTH_LONG
        ).setAction(R.string.undo) {
            viewModel.undoRemoveTranslation()
        }.show()
    }
}