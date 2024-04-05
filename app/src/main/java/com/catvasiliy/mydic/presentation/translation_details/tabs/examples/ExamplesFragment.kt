package com.catvasiliy.mydic.presentation.translation_details.tabs.examples

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.catvasiliy.mydic.databinding.TabFragmentExamplesBinding
import com.catvasiliy.mydic.domain.model.translation.Example
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsState
import com.catvasiliy.mydic.presentation.translation_details.TranslationDetailsViewModel
import com.catvasiliy.mydic.presentation.util.hideAndShowOther
import com.catvasiliy.mydic.presentation.util.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ExamplesFragment : Fragment() {

    private var _binding: TabFragmentExamplesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TranslationDetailsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TabFragmentExamplesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setupView()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collectLatest { state ->
                    val examples = if (state is TranslationDetailsState.Translation) {
                        state.translation.examples
                    } else {
                        emptyList()
                    }
                    updateView(examples)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        binding.rvExamples.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun updateView(examples: List<Example>): Unit = with(binding) {
        if (examples.isEmpty()) {
            rvExamples.hideAndShowOther(binding.clNoExamples)
            return
        }

        rvExamples.apply {
            adapter = ExamplesAdapter(examples)
            show()
        }
    }
}