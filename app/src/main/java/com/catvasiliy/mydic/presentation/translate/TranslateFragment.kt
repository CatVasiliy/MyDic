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
import androidx.navigation.fragment.findNavController
import com.catvasiliy.mydic.R
import com.catvasiliy.mydic.databinding.FragmentTranslateBinding
import com.catvasiliy.mydic.presentation.translate.spinners.SourceLanguageSpinnerAdapter
import com.catvasiliy.mydic.presentation.translate.spinners.SourceLanguageSpinnerItem
import com.catvasiliy.mydic.presentation.translate.spinners.TargetLanguageSpinnerAdapter
import com.catvasiliy.mydic.presentation.translate.spinners.TargetLanguageSpinnerItem
import com.catvasiliy.mydic.presentation.util.setSelectionWithTag
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TranslateFragment : Fragment() {

    private var _binding: FragmentTranslateBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslateViewModel by viewModels()

    private val slDefaultItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val newSourceLanguageItem = parent.getItemAtPosition(position) as SourceLanguageSpinnerItem
            val newSourceLanguage = newSourceLanguageItem.language
            if (binding.spSourceLanguage.tag != position) {
                viewModel.updateDefaultSourceLanguage(newSourceLanguage)
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) { }
    }

    private val tlDefaultItemSelectedListener = object : OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
            val newTargetLanguageItem = parent.getItemAtPosition(position) as TargetLanguageSpinnerItem
            val newTargetLanguage = newTargetLanguageItem.language
            if (binding.spTargetLanguage.tag != position) {
                viewModel.updateDefaultTargetLanguage(newTargetLanguage)
            }
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

        binding.tbTranslation.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        arguments?.getString("sourceText")?.let { sourceText ->
            binding.etSource.setText(sourceText)
        }
        arguments?.remove("sourceText")

        val slSpinnerAdapter = SourceLanguageSpinnerAdapter(requireContext())
        binding.spSourceLanguage.apply {
            adapter = slSpinnerAdapter
            onItemSelectedListener = slDefaultItemSelectedListener
        }

        val tlSpinnerAdapter = TargetLanguageSpinnerAdapter(requireContext())
        binding.spTargetLanguage.apply {
            adapter = tlSpinnerAdapter
            onItemSelectedListener = tlDefaultItemSelectedListener
        }

        binding.btnTranslate.setOnClickListener {
            val sourceText = binding.etSource.text.toString()
            if (sourceText.isBlank()) {
                Snackbar.make(
                    binding.root,
                    R.string.empty_source_snackbar,
                    Snackbar.LENGTH_LONG
                ).show()
                return@setOnClickListener
            }

            val slSelectedItem = binding.spSourceLanguage.selectedItem as SourceLanguageSpinnerItem
            val tlSelectedItem = binding.spTargetLanguage.selectedItem as TargetLanguageSpinnerItem

            val sourceLanguage = slSelectedItem.language
            val targetLanguage = tlSelectedItem.language

            val action = TranslateFragmentDirections.openTranslationDetailsFromTranslate(
                sourceText = binding.etSource.text.toString(),
                sourceLanguageOrdinal = sourceLanguage?.ordinal ?: -1,
                targetLanguage = targetLanguage
            )
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val slSelection = state.languagePreferences.defaultSourceLanguage
                    val slPosition = slSpinnerAdapter.getPosition(slSelection)
                    binding.spSourceLanguage.setSelectionWithTag(slPosition)

                    val tlSelection = state.languagePreferences.defaultTargetLanguage
                    val tlPosition = tlSpinnerAdapter.getPosition(tlSelection)
                    binding.spTargetLanguage.setSelectionWithTag(tlPosition)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}