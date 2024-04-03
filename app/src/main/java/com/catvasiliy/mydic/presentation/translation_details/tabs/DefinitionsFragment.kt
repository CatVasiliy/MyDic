package com.catvasiliy.mydic.presentation.translation_details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.catvasiliy.mydic.databinding.ItemDefinitionBinding
import com.catvasiliy.mydic.databinding.TabFragmentDefinitionsBinding
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsState
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.show
import com.catvasiliy.mydic.presentation.util.showIf
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DefinitionsFragment : Fragment() {

    private var _binding: TabFragmentDefinitionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationDetailsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFragmentDefinitionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val definitions = if (state is TranslationDetailsState.Translation) {
                        state.translation.definitions
                    } else {
                        emptyList()
                    }
                    updateView(definitions)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateView(definitions: List<Definition>) {
        if (definitions.isEmpty()) {
            binding.svDefinitions.hideAndShowOther(binding.llNoDefinitions)
            return
        }

        binding.svDefinitions.show()
        definitions.forEach { definition ->
            val definitionBinding = ItemDefinitionBinding.inflate(layoutInflater)
            with(definitionBinding) {
                tvDefinitionPartOfSpeech.text = definition.partOfSpeech
                tvDefinition.text = definition.definitionText

                val exampleText = definition.exampleText
                dividerNoExample.showIf { exampleText == null }
                tvDefinitionExampleTitle.showIf { exampleText != null }
                tvDefinitionExample.apply {
                    showIf { exampleText != null }
                    text = exampleText
                }
                dividerDefinition.showIf { exampleText != null }
            }
            binding.llDefinitions.addView(definitionBinding.root)
        }
    }
}