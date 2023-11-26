package com.catvasiliy.mydic.presentation.translation_details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.catvasiliy.mydic.databinding.DefinitionItemBinding
import com.catvasiliy.mydic.databinding.FragmentDefinitionsBinding
import com.catvasiliy.mydic.domain.model.translation.Definition
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DefinitionsFragment : Fragment() {

    private var _binding: FragmentDefinitionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationDetailsViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDefinitionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                val definitions = if (state.translation is ExtendedTranslation) {
                    state.translation.definitions
                } else {
                    emptyList()
                }
                createDefinitionViews(definitions)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createDefinitionViews(definitions: List<Definition>) {
        if (definitions.isEmpty()) {
            binding.svDefinitions.visibility = GONE
            binding.llNoDefinitions.visibility = VISIBLE
            return
        }

        binding.svDefinitions.visibility = VISIBLE

        definitions.forEach { definition ->
            val definitionBinding = DefinitionItemBinding.inflate(layoutInflater)
            definitionBinding.apply {
                tvDefinitionPartOfSpeech.text = definition.partOfSpeech
                tvDefinition.text = definition.definitionText
                tvDefinitionExample.text = definition.exampleText
            }
            binding.llDefinitions.addView(definitionBinding.root)
        }
    }
}