package com.catvasiliy.mydic.presentation.translations_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.catvasiliy.mydic.databinding.BottomSheetSortBinding
import com.catvasiliy.mydic.databinding.FragmentTranslationsListBinding
import com.catvasiliy.mydic.domain.model.translation.Translation
import com.catvasiliy.mydic.presentation.MainActivity
import com.catvasiliy.mydic.presentation.util.SortType
import com.catvasiliy.mydic.presentation.util.TranslationSort
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
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

    private var _bottomSheetSortBinding: BottomSheetSortBinding? = null
    private val bottomSheetSortBinding get() = _bottomSheetSortBinding!!

    private val translationsListAdapter = TranslationsListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslationsListBinding.inflate(inflater, container, false)
        _bottomSheetSortBinding = BottomSheetSortBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tbTranslations.setNavigationOnClickListener {
            (requireActivity() as MainActivity).openNavigationDrawer()
        }

        val swipeHelper = ItemTouchHelper(
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
        )

        binding.rvTranslations.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = translationsListAdapter
            swipeHelper.attachToRecyclerView(this)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    showTranslations(state.translations)
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is TranslationsListUiEvent.ShowUndoDeleteSnackbar -> {
                            Snackbar.make(
                                binding.root,
                                R.string.delete_translation_snackbar,
                                Snackbar.LENGTH_LONG
                            ).setAction(R.string.undo) {
                                viewModel.undoRemoveTranslation()
                            }.show()
                        }
                    }
                }
            }
        }
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

        binding.fabTranslate.setOnClickListener {
            val action = TranslationsListFragmentDirections.openTranslate()
            findNavController().navigate(action)
        }

        val bottomSheetSort = setupAndGetBottomSheetSort()

        setupChips(bottomSheetSort)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _bottomSheetSortBinding = null
        _binding = null
    }

    private fun showTranslations(translationsList: List<Translation>) {
        if (translationsList.isNotEmpty()) {
            translationsListAdapter.submitList(translationsList)
            binding.llNoTranslations.hideAndShowOther(binding.llTranslations)
        } else {
            binding.llTranslations.hideAndShowOther(binding.llNoTranslations)
        }
    }

    private fun setupAndGetBottomSheetSort(): BottomSheetDialog {
        val bottomSheetSort = BottomSheetDialog(requireContext()).apply {
            setContentView(bottomSheetSortBinding.root)
        }

        bottomSheetSortBinding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val sortType = viewModel.state.value.sortInfo.sortType
            val sortInfo = when (checkedId) {
                R.id.rbDate -> TranslationSort.Date(sortType)
                R.id.rbSourceText -> TranslationSort.SourceText(sortType)
                R.id.rbTranslationText -> TranslationSort.TranslationText(sortType)
                else -> {
                    bottomSheetSort.dismiss()
                    return@setOnCheckedChangeListener
                }
            }
            viewModel.sortTranslations(sortInfo)
            changeSortByChipText()
            bottomSheetSort.dismiss()
        }

        return bottomSheetSort
    }

    private fun setupChips(bottomSheetSort: BottomSheetDialog) {
        changeSortByChipText()
        binding.chipSortBy.setOnClickListener {
            bottomSheetSort.show()
        }

        binding.chipDescending.setOnCheckedChangeListener { _, isChecked ->
            val sortInfo = viewModel.state.value.sortInfo
            sortInfo.sortType = if (isChecked) SortType.Descending else SortType.Ascending
            viewModel.sortTranslations(sortInfo)
        }
    }

    private fun changeSortByChipText() {
        val radioButtonText = when (bottomSheetSortBinding.radioGroup.checkedRadioButtonId) {
            R.id.rbDate -> getString(R.string.date)
            R.id.rbSourceText -> getString(R.string.source_text)
            R.id.rbTranslationText -> getString(R.string.translation_text)
            else -> return
        }
        binding.chipSortBy.text = getString(R.string.sort_by_chip, radioButtonText)
    }
}