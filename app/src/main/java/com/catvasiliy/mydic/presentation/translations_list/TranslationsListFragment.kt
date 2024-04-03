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
import com.catvasiliy.mydic.presentation.model.preferences.translation_organizing.UiTranslationOrganizingPreferences
import com.catvasiliy.mydic.presentation.model.translation.UiTranslationListItem
import com.catvasiliy.mydic.presentation.translations_list.list_util.SwipeToDeleteCallback
import com.catvasiliy.mydic.presentation.translations_list.list_util.TranslationsListAdapter
import com.catvasiliy.mydic.presentation.translations_list.spinner.SourceLanguageFilterSpinnerAdapter
import com.catvasiliy.mydic.presentation.translations_list.spinner.SourceLanguageFilterSpinnerItem
import com.catvasiliy.mydic.presentation.translations_list.spinner.TargetLanguageFilterSpinnerAdapter
import com.catvasiliy.mydic.presentation.translations_list.spinner.TargetLanguageFilterSpinnerItem
import com.catvasiliy.mydic.presentation.translations_list.state.TranslationsListVisibility
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

    private val translationsListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TranslationsListAdapter()
    }

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

    private val swipeHelper by lazy(LazyThreadSafetyMode.NONE) {
        ItemTouchHelper(swipeToDeleteCallback)
    }

    private var _bottomSheetOrganizeBinding: BottomSheetOrganizeBinding? = null
    private val bottomSheetOrganizeBinding get() = _bottomSheetOrganizeBinding!!

    private val bottomSheetOrganize by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetDialog(requireContext())
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
        bottomSheetOrganize.setContentView(bottomSheetOrganizeBinding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    updateOrganizingPreferences(state.organizingPreferences)
                    updateTranslations(
                        translationsList = state.translations,
                        listVisibility = state.listVisibility
                    )
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
        setupMenuItems()
        setupBottomSheetOrganize()

//        val swipeHelper = ItemTouchHelper(swipeToDeleteCallback)

        with(binding) {
            rvTranslations.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = translationsListAdapter
                addOnScrollListener(translationsListScrollListener)
                swipeHelper.attachToRecyclerView(this)
            }

            fabTranslate.setOnClickListener {
                val action = TranslationsListFragmentDirections.openTranslate()
                findNavController().navigate(action)
            }
        }
    }

    private fun setupMenuItems() {
        setupSearch()

        with(binding.tbTranslations.menu) {
            findItem(R.id.miOrganize).setOnMenuItemClickListener {
                bottomSheetOrganize.show()
                true
            }
            findItem(R.id.miSettings).setOnMenuItemClickListener {
                val action = TranslationsListFragmentDirections.openSettings()
                findNavController().navigate(action)
                true
            }
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

    private fun setupBottomSheetOrganize(): Unit = with(bottomSheetOrganizeBinding) {

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (radioGroup.tag == checkedId) return@setOnCheckedChangeListener

            val sortingOrder = viewModel.state.value.organizingPreferences.sortingInfo.sortingOrder
            val newSortingInfo = when (checkedId) {
                R.id.rbDate -> TranslationSortingInfo.Date(sortingOrder)
                R.id.rbSourceText -> TranslationSortingInfo.SourceText(sortingOrder)
                R.id.rbTranslationText -> TranslationSortingInfo.TranslationText(sortingOrder)
                else -> return@setOnCheckedChangeListener
            }

            viewModel.sortTranslations(newSortingInfo)
        }

        cbDescending.setOnCheckedChangeListener { _, isChecked ->
            val currentSortingInfo = viewModel.state.value.organizingPreferences.sortingInfo
            val newSortingOrder = if (isChecked) SortingOrder.Descending else SortingOrder.Ascending
            val currentSortingOrder = currentSortingInfo.sortingOrder
            if (newSortingOrder != currentSortingOrder) {
                val sortingInfo = currentSortingInfo.copyChangeSortingOrder(newSortingOrder)
                viewModel.sortTranslations(sortingInfo)
            }
        }

        spSourceLanguage.apply {
            adapter = slFilterAdapter
            onItemSelectedListener = slItemSelectedListener
        }

        spTargetLanguage.apply {
            adapter = tlFilterAdapter
            onItemSelectedListener = tlItemSelectedListener
        }

        btnReset.setOnClickListener {
            viewModel.resetOrganizingPreferences()
        }
    }

    private fun updateTranslations(
        translationsList: List<UiTranslationListItem>,
        listVisibility: TranslationsListVisibility
    ) {
        when(listVisibility) {
            is TranslationsListVisibility.Visible -> updateTranslationsList(translationsList)
            is TranslationsListVisibility.Gone -> updateNoTranslations(listVisibility)
        }
    }

    private fun updateTranslationsList(translationsList: List<UiTranslationListItem>): Unit = with(binding) {
        translationsListAdapter.submitList(translationsList)

        // Reattach ItemTouchHelper to make sure all items that deletion was undone would draw properly
        // Problem is caused by inability of ItemTouchHelper to restore item's original position
        // Problem only appears if there is only one item in list
        if (translationsList.size == 1) {
            swipeHelper.attachToRecyclerView(null)
            swipeHelper.attachToRecyclerView(rvTranslations)
        }

        llNoTranslations.hideAndShowOther(llTranslations)
    }

    private fun updateNoTranslations(noTranslationsCause: TranslationsListVisibility.Gone) = with(binding) {
        ivNoTranslations.setImageResource(noTranslationsCause.drawableResId)
        tvNoTranslations.setText(noTranslationsCause.stringResId)
        llTranslations.hideAndShowOther(llNoTranslations)
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

        with(bottomSheetOrganizeBinding) {
            spSourceLanguage.setSelectionWithTag(slFilteringPosition)
            spTargetLanguage.setSelectionWithTag(tlFilteringPosition)

            btnReset.isEnabled = organizingPreferences.isDefault.not()
        }
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
            is TranslationsListUiEvent.ScrollListToTop -> binding.rvTranslations.scrollToTop()
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

    private fun RecyclerView.scrollToTop() = post {
        scrollToPosition(0)
        binding.fabTranslate.apply {
            if (!isShown) show()
        }
    }
}