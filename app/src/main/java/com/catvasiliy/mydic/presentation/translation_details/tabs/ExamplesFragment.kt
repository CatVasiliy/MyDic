package com.catvasiliy.mydic.presentation.translation_details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.catvasiliy.mydic.databinding.ExampleItemBinding
import com.catvasiliy.mydic.databinding.FragmentExamplesBinding
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.domain.model.translation.ExtendedTranslation
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExamplesFragment : Fragment() {

    private var _binding: FragmentExamplesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationDetailsViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExamplesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                val examples = if (state.translation is ExtendedTranslation) {
                    state.translation.examples
                } else {
                    emptyList()
                }
                createExamplesViews(examples)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createExamplesViews(examples: List<Example>) {
        if (examples.isEmpty()) {
            binding.svExamples.visibility = GONE
            binding.llNoExamples.visibility = VISIBLE
            return
        }

        binding.svExamples.visibility = VISIBLE

        examples.forEach { example ->
            val exampleBinding = ExampleItemBinding.inflate(layoutInflater)
            exampleBinding.tvExample.text = HtmlCompat.fromHtml(
                example.exampleText,
                FROM_HTML_MODE_COMPACT
            )
            binding.llExamples.addView(exampleBinding.root)
        }
    }
}