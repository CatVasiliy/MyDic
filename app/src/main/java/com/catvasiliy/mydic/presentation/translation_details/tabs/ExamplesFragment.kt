package com.catvasiliy.mydic.presentation.translation_details.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_MODE_COMPACT
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.catvasiliy.mydic.databinding.FragmentExamplesBinding
import com.catvasiliy.mydic.databinding.ItemExampleBinding
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.show
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val examples = state.translation?.examples ?: emptyList()
                    createExamplesViews(examples)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createExamplesViews(examples: List<Example>) {
        if (examples.isEmpty()) {
            binding.svExamples.hideAndShowOther(binding.llNoExamples)
            return
        }

        binding.svExamples.show()

        examples.forEach { example ->
            val exampleBinding = ItemExampleBinding.inflate(layoutInflater)
            exampleBinding.tvExample.text = HtmlCompat.fromHtml(
                example.exampleText,
                FROM_HTML_MODE_COMPACT
            )
            binding.llExamples.addView(exampleBinding.root)
        }
    }
}