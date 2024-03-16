package com.catvasiliy.mydic.presentation.translate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.FragmentTranslateBinding
import com.catvasiliy.mydic.presentation.model.preferences.UiLanguagePreferences
import com.catvasiliy.mydic.presentation.translate.spinners.SourceLanguageSpinnerAdapter
import com.catvasiliy.mydic.presentation.translate.spinners.SourceLanguageSpinnerItem
import com.catvasiliy.mydic.presentation.translate.spinners.TargetLanguageSpinnerAdapter
import com.catvasiliy.mydic.presentation.translate.spinners.TargetLanguageSpinnerItem
import com.catvasiliy.mydic.presentation.util.setSelectionWithTag
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val KEY_EXTERNAL_SOURCE_TEXT = "EXTERNAL_SOURCE_TEXT"

@AndroidEntryPoint
class TranslateFragment : Fragment() {

    private var _binding: FragmentTranslateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslateViewModel by viewModels()

    private val sourceLanguageAdapter by lazy(LazyThreadSafetyMode.NONE) {
        SourceLanguageSpinnerAdapter(requireContext())
    }
    private val slItemSelectedListener = object : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (binding.spSourceLanguage.tag == position) return

            val newSourceLanguageItem = parent.getItemAtPosition(position) as SourceLanguageSpinnerItem
            val newSourceLanguage = newSourceLanguageItem.language
            viewModel.updateDefaultSourceLanguage(newSourceLanguage)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
    }


    private val targetLanguageAdapter by lazy(LazyThreadSafetyMode.NONE) {
        TargetLanguageSpinnerAdapter(requireContext())
    }
    private val tlItemSelectedListener = object : OnItemSelectedListener {

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            if (binding.spTargetLanguage.tag == position) return

            val newTargetLanguageItem = parent.getItemAtPosition(position) as TargetLanguageSpinnerItem
            val newTargetLanguage = newTargetLanguageItem.language
            viewModel.updateDefaultTargetLanguage(newTargetLanguage)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTranslateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        handleNavArgs()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    updateLanguagesView(state.languagePreferences)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.tbTranslation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.spSourceLanguage.apply {
            adapter = sourceLanguageAdapter
            onItemSelectedListener = slItemSelectedListener
        }

        binding.spTargetLanguage.apply {
            adapter = targetLanguageAdapter
            onItemSelectedListener = tlItemSelectedListener
        }

        binding.btnTranslate.setOnClickListener {
            getOpenTranslationDetailsAction()?.let { action ->
                findNavController().navigate(action)
            }
        }
    }

    private fun getOpenTranslationDetailsAction(): NavDirections? {
        val sourceText = binding.etSource.text.toString()
        if (sourceText.isBlank()) {
            showEmptySourceTextSnackbar()
            return null
        }

        val slSelectedItem = binding.spSourceLanguage.selectedItem as SourceLanguageSpinnerItem
        val tlSelectedItem = binding.spTargetLanguage.selectedItem as TargetLanguageSpinnerItem

        val sourceLanguage = slSelectedItem.language
        val targetLanguage = tlSelectedItem.language

        return TranslateFragmentDirections.openTranslationDetailsFromTranslate(
            sourceText = binding.etSource.text.toString(),
            sourceLanguageOrdinal = sourceLanguage?.ordinal ?: -1,
            targetLanguage = targetLanguage
        )
    }

    private fun showEmptySourceTextSnackbar() {
        Snackbar.make(
            binding.root,
            R.string.empty_source_snackbar,
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleNavArgs() {
        arguments?.getString(KEY_EXTERNAL_SOURCE_TEXT)?.let { sourceText ->
            binding.etSource.setText(sourceText)
        }
        arguments?.remove(KEY_EXTERNAL_SOURCE_TEXT)
    }

    private fun updateLanguagesView(languagePreferences: UiLanguagePreferences) {
        val slSelection = languagePreferences.defaultSourceLanguage
        val slPosition = sourceLanguageAdapter.getPosition(slSelection)
        binding.spSourceLanguage.setSelectionWithTag(slPosition)

        val tlSelection = languagePreferences.defaultTargetLanguage
        val tlPosition = targetLanguageAdapter.getPosition(tlSelection)
        binding.spTargetLanguage.setSelectionWithTag(tlPosition)
    }
}